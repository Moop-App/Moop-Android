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
package soup.movie.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import soup.movie.common.DefaultDispatcher
import soup.movie.core.ads.AdsManager
import soup.movie.core.ads.NativeAdInfo
import soup.movie.core.analytics.EventAnalytics
import soup.movie.core.imageloading.ImageUriProvider
import soup.movie.data.repository.MovieRepository
import soup.movie.domain.movie.MM_DD
import soup.movie.domain.movie.screenDays
import soup.movie.domain.movie.yesterday
import soup.movie.log.Logger
import soup.movie.model.MovieDetailModel
import soup.movie.model.MovieModel
import soup.movie.model.OpenDateAlarmModel
import soup.movie.model.TheaterRatingsModel
import soup.movie.resources.R
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository,
    private val analytics: EventAnalytics,
    private val imageUriProvider: ImageUriProvider,
    private val adsManager: AdsManager,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val movieId: String = savedStateHandle["movieId"]!!

    private val _uiModel = MutableStateFlow<DetailUiModel>(DetailUiModel.None)
    val uiModel: StateFlow<DetailUiModel> = _uiModel

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            _isFavorite.emit(repository.isFavoriteMovie(movieId))
            loadDetail(movieId)
            adsManager.onNativeAdConsumed()
            adsManager.loadNextNativeAd()
        }
    }

    private suspend fun loadDetail(movieId: String) {
        try {
            val detail = repository.getMovieDetail(movieId)
            val adInfo = adsManager.getLoadedNativeAd()
            renderDetail(detail, adInfo)
        } catch (t: Throwable) {
            Logger.w(t)
            _uiModel.emit(DetailUiModel.Failure)
        }
    }

    private suspend fun renderDetail(
        detail: MovieDetailModel,
        adInfo: NativeAdInfo?,
    ) {
        withContext(defaultDispatcher) {
            _uiModel.emit(
                DetailUiModel.Success(
                    header = HeaderUiModel(
                        movie = detail.toMovie(),
                        showTm = detail.showTm ?: 0,
                        nations = detail.nations.orEmpty(),
                        companies = detail.companies.orEmpty(),
                    ),
                    items = detail.toItemsUiModel(adInfo),
                ),
            )
        }
    }

    fun requestShareImage(imageUrl: String) {
        viewModelScope.launch {
            val uri = imageUriProvider(imageUrl)
            _uiEvent.emit(
                if (uri != null) {
                    ShareImageAction(uri, "image/*")
                } else {
                    ToastAction(R.string.action_share_poster_failed)
                },
            )
        }
    }

    private fun MovieDetailModel.toMovie(): MovieModel {
        return MovieModel(
            id = id,
            score = score,
            title = title,
            posterUrl = posterUrl,
            openDate = openDate,
            isNow = isNow,
            age = age,
            nationFilter = nationFilter,
            genres = genres,
            boxOffice = boxOffice?.rank,
            theater = TheaterRatingsModel(
                cgv = cgv?.star,
                lotte = lotte?.star,
                megabox = megabox?.star,
            ),
        )
    }

    private fun MovieDetailModel.toItemsUiModel(adInfo: NativeAdInfo?): List<ContentItemUiModel> {
        val items = mutableListOf<ContentItemUiModel>()
        boxOffice?.run {
            items.add(
                BoxOfficeItemUiModel(
                    rank = rank,
                    rankDate = yesterday().MM_DD(),
                    audience = audiAcc,
                    screenDays = screenDays(),
                    rating = naver?.star ?: NO_RATING,
                    webLink = naver?.url,
                ),
            )
        }
        imdb?.run {
            items.add(
                ImdbItemUiModel(
                    imdb = star,
                    rottenTomatoes = rt?.star ?: NO_RATING,
                    metascore = mc?.star ?: NO_RATING,
                    webLink = url,
                ),
            )
        }
        items.add(
            TheatersItemUiModel(
                cgv = CgvItemUiModel(
                    movieId = cgv?.id.orEmpty(),
                    hasInfo = cgv != null,
                    rating = cgv?.star ?: NO_RATING,
                    webLink = cgv?.url,
                ),
                lotte = LotteItemUiModel(
                    movieId = lotte?.id.orEmpty(),
                    hasInfo = lotte != null,
                    rating = lotte?.star ?: NO_RATING,
                    webLink = lotte?.url,
                ),
                megabox = MegaboxItemUiModel(
                    movieId = megabox?.id.orEmpty(),
                    hasInfo = megabox != null,
                    rating = megabox?.star ?: NO_RATING,
                    webLink = megabox?.url,
                ),
            ),
        )
        if (boxOffice == null) {
            naver?.run {
                items.add(
                    NaverItemUiModel(
                        rating = star,
                        webLink = url,
                    ),
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
                    query = "감독 $it",
                )
            },
        )
        persons.addAll(
            actors.orEmpty().map {
                val cast = if (it.cast.isEmpty()) "출연" else it.cast
                PersonUiModel(
                    name = it.peopleNm,
                    cast = cast,
                    query = "배우 ${it.peopleNm}",
                )
            },
        )
        if (persons.isNotEmpty()) {
            items.add(CastItemUiModel(persons = persons))
        }

        adInfo?.let {
            items.add(AdItemUiModel(it))
        }

        val trailers = trailers.orEmpty()
        if (trailers.isNotEmpty()) {
            items.add(TrailerHeaderItemUiModel(movieTitle = title))
            items.addAll(
                trailers.map {
                    TrailerItemUiModel(trailer = it)
                },
            )
            items.add(TrailerFooterItemUiModel(movieTitle = title))
        }
        return items
    }

    fun onFavoriteButtonClick(isFavorite: Boolean) {
        val movie = (_uiModel.value as? DetailUiModel.Success)?.header?.movie ?: return
        viewModelScope.launch {
            if (isFavorite) {
                repository.addFavoriteMovie(movie)
                if (movie.isPlan) {
                    repository.insertOpenDateAlarms(
                        OpenDateAlarmModel(
                            movie.id,
                            movie.title,
                            movie.openDate,
                        ),
                    )
                    _uiEvent.emit(ToastAction(R.string.action_toast_opendate_alarm))
                }
            } else {
                repository.removeFavoriteMovie(movie.id)
            }
            _isFavorite.emit(isFavorite)
        }
    }

    fun onRetryClick() {
        viewModelScope.launch {
            loadDetail(movieId)
        }
    }

    fun clickPoster() {
        analytics.clickPoster()
    }

    fun clickShare() {
        analytics.clickShare()
    }

    fun clickTrailer() {
        analytics.clickTrailer()
    }

    fun clickMoreTrailers() {
        analytics.clickMoreTrailers()
    }

    fun clickCgvInfo() {
        analytics.clickCgvInfo()
    }

    fun clickLotteInfo() {
        analytics.clickLotteInfo()
    }

    fun clickMegaboxInfo() {
        analytics.clickMegaboxInfo()
    }

    companion object {
        private const val NO_RATING = "평점없음"
    }
}
