/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.nativead.NativeAd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import soup.movie.ads.AdsManager
import soup.movie.device.ImageUriProvider
import soup.movie.ext.screenDays
import soup.movie.model.Movie
import soup.movie.model.MovieDetail
import soup.movie.model.OpenDateAlarm
import soup.movie.model.repository.MovieRepository
import soup.movie.ui.EventLiveData
import soup.movie.ui.MutableEventLiveData
import soup.movie.util.MM_DD
import soup.movie.util.yesterday
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val imageUriProvider: ImageUriProvider,
    private val adsManager: AdsManager
) : ViewModel() {

    private lateinit var movie: Movie
    private var movieDetail: MovieDetail? = null
    private var nativeAd: NativeAd? = null

    private val _headerUiModel = MutableLiveData<HeaderUiModel>()
    val headerUiModel: LiveData<HeaderUiModel>
        get() = _headerUiModel

    private val _contentUiModel = MutableLiveData<ContentUiModel>()
    val contentUiModel: LiveData<ContentUiModel>
        get() = _contentUiModel

    private val _favoriteUiModel = MutableLiveData<Boolean>()
    val favoriteUiModel: LiveData<Boolean>
        get() = _favoriteUiModel

    private val _uiEvent = MutableEventLiveData<UiEvent>()
    val uiEvent: EventLiveData<UiEvent>
        get() = _uiEvent

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean>
        get() = _isError

    fun init(movie: Movie) {
        this.movie = movie
        _headerUiModel.value = HeaderUiModel(movie)
        viewModelScope.launch {
            // FIXME: Elvis operator (?:) is used because of lint rule error https://issuetracker.google.com/issues/169249668
            _favoriteUiModel.postValue(repository.isFavoriteMovie(movie.id) ?: false)
            val minDelay = async { delay(500) }
            val loadDetail = async {
                loadDetail(movie)
            }
            minDelay.await()
            movieDetail = loadDetail.await()?.also {
                renderDetail(it, getNativeAd())
            }
            withContext(Dispatchers.IO) {
                adsManager.onNativeAdConsumed()
                adsManager.loadNextNativeAd()
            }
        }
    }

    override fun onCleared() {
        nativeAd?.destroy()
        super.onCleared()
    }

    private suspend fun loadDetail(movie: Movie): MovieDetail? {
        _isError.postValue(false)
        try {
            return withContext(Dispatchers.IO) {
                repository.getMovieDetail(movie.id)
            }
        } catch (t: Throwable) {
            Timber.w(t)
            _isError.postValue(true)
        }
        return null
    }

    private suspend fun renderDetail(
        detail: MovieDetail,
        nativeAd: NativeAd?
    ) = withContext(Dispatchers.Default) {
        _headerUiModel.postValue(
            HeaderUiModel(
                movie = movie,
                showTm = detail.showTm ?: 0,
                nations = detail.nations.orEmpty(),
                companies = detail.companies.orEmpty()
            )
        )
        _contentUiModel.postValue(detail.toContentUiModel(nativeAd))
    }

    fun requestShareText(target: ShareTarget) {
        viewModelScope.launch {
            _uiEvent.event = ShareAction.Text(target)
        }
    }

    fun requestShareImage(target: ShareTarget, imageUrl: String) {
        viewModelScope.launch {
            val uri = imageUriProvider(imageUrl)
            _uiEvent.event = if (uri != null) {
                ShareAction.Image(target, uri, "image/*")
            } else {
                ToastAction(R.string.action_share_poster_failed)
            }
        }
    }

    private fun MovieDetail.toContentUiModel(nativeAd: NativeAd?): ContentUiModel {
        val items = mutableListOf<ContentItemUiModel>()
        items.add(HeaderItemUiModel)
        boxOffice?.run {
            items.add(
                BoxOfficeItemUiModel(
                    rank = rank,
                    rankDate = yesterday().MM_DD(),
                    audience = audiAcc,
                    screenDays = screenDays(),
                    rating = naver?.star ?: NO_RATING,
                    webLink = naver?.url
                )
            )
        }
        imdb?.run {
            items.add(
                ImdbItemUiModel(
                    imdb = star,
                    rottenTomatoes = rt?.star ?: NO_RATING,
                    metascore = mc?.star ?: NO_RATING,
                    webLink = url
                )
            )
        }
        items.add(
            CgvItemUiModel(
                movieId = cgv?.id.orEmpty(),
                hasInfo = cgv != null,
                rating = cgv?.star ?: NO_RATING,
                webLink = cgv?.url
            )
        )
        items.add(
            LotteItemUiModel(
                movieId = lotte?.id.orEmpty(),
                hasInfo = lotte != null,
                rating = lotte?.star ?: NO_RATING,
                webLink = lotte?.url
            )
        )
        items.add(
            MegaboxItemUiModel(
                movieId = megabox?.id.orEmpty(),
                hasInfo = megabox != null,
                rating = megabox?.star ?: NO_RATING,
                webLink = megabox?.url
            )
        )
        if (boxOffice == null) {
            naver?.run {
                items.add(
                    NaverItemUiModel(
                        rating = star,
                        webLink = url
                    )
                )
            }
        }

        val plot = plot.orEmpty()
        if (plot.isNotBlank()) {
            items.add(PlotItemUiModel(plot = plot))
        }

        val persons = mutableListOf<PersonUiModel>()
        persons.addAll(
            directors.orEmpty().map {
                PersonUiModel(
                    name = it,
                    cast = "감독",
                    query = "감독 $it"
                )
            }
        )
        persons.addAll(
            actors.orEmpty().map {
                val cast = if (it.cast.isEmpty()) "출연" else it.cast
                PersonUiModel(
                    name = it.peopleNm,
                    cast = cast,
                    query = "배우 ${it.peopleNm}"
                )
            }
        )
        if (persons.isNotEmpty()) {
            items.add(CastItemUiModel(persons = persons))
        }

        nativeAd?.let {
            items.add(AdItemUiModel(it))
        }

        val trailers = trailers.orEmpty()
        if (trailers.isNotEmpty()) {
            items.add(TrailerHeaderItemUiModel(movieTitle = title))
            items.addAll(
                trailers.map {
                    TrailerItemUiModel(trailer = it)
                }
            )
            items.add(TrailerFooterItemUiModel(movieTitle = title))
        }
        return ContentUiModel(items)
    }

    fun onFavoriteButtonClick(isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isFavorite) {
                repository.addFavoriteMovie(movie)
                if (movie.isPlan) {
                    repository.insertOpenDateAlarms(
                        OpenDateAlarm(
                            movie.id,
                            movie.title,
                            movie.openDate
                        )
                    )
                    _uiEvent.postEvent(ToastAction(R.string.action_toast_opendate_alarm))
                }
            } else {
                repository.removeFavoriteMovie(movie.id)
            }
            _favoriteUiModel.postValue(isFavorite)
        }
    }

    fun onRetryClick() {
        viewModelScope.launch {
            movieDetail = loadDetail(movie)?.also { renderDetail(it, getNativeAd()) }
        }
    }

    private fun getNativeAd(): NativeAd? {
        return adsManager.getLoadedNativeAd()
    }

    companion object {

        private const val NO_RATING = "평점없음"
    }
}
