package soup.movie.detail

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.*
import soup.movie.ads.AdsManager
import soup.movie.device.ImageUriProvider
import soup.movie.ext.screenDays
import soup.movie.model.Movie
import soup.movie.model.MovieDetail
import soup.movie.model.OpenDateAlarm
import soup.movie.model.repository.MoopRepository
import soup.movie.ui.EventLiveData
import soup.movie.ui.MutableEventLiveData
import soup.movie.util.MM_DD
import soup.movie.util.yesterday

class DetailViewModel @AssistedInject constructor(
    private val repository: MoopRepository,
    private val imageUriProvider: ImageUriProvider,
    private val adsManager: AdsManager
) : ViewModel() {

    private lateinit var movie: Movie
    private var movieDetail: MovieDetail? = null
    private var nativeAd: UnifiedNativeAd? = null

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

    private val _isError = MutableLiveData<Boolean>(false)
    val isError: LiveData<Boolean>
        get() = _isError

    fun init(movie: Movie) {
        this.movie = movie
        _headerUiModel.value = HeaderUiModel(movie)
        viewModelScope.launch {
            _favoriteUiModel.postValue(repository.isFavoriteMovie(movie.id))
            val minDelay = async { delay(500) }
            val loadDetail = async {
                loadDetail(movie)
            }
            minDelay.await()
            movieDetail = loadDetail.await()?.also { renderDetail(it) }

            withContext(Dispatchers.IO) {
                nativeAd = adsManager.loadNativeAd()
                    ?.takeIf { it.icon != null } // 간단한 유효성 검사
                    ?.also { ad ->
                        movieDetail?.let { detail -> renderDetail(detail, ad) }
                    }
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
            _isError.postValue(true)
        }
        return null
    }

    private suspend fun renderDetail(
        detail: MovieDetail,
        nativeAd: UnifiedNativeAd? = null
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

    fun requestShareImage(target: ShareTarget, bitmap: Bitmap) {
        viewModelScope.launch {
            val uri = imageUriProvider(bitmap)
            _uiEvent.event = ShareAction(target, uri, "image/*")
        }
    }

    private fun MovieDetail.toContentUiModel(nativeAd: UnifiedNativeAd?): ContentUiModel {
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
                rating = cgv?.star ?: NO_RATING
            )
        )
        items.add(
            LotteItemUiModel(
                movieId = lotte?.id.orEmpty(),
                hasInfo = lotte != null,
                rating = lotte?.star ?: NO_RATING
            )
        )
        items.add(
            MegaboxItemUiModel(
                movieId = megabox?.id.orEmpty(),
                hasInfo = megabox != null,
                rating = megabox?.star ?: NO_RATING
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
        persons.addAll(directors.orEmpty().map {
            PersonUiModel(
                name = it,
                cast = "감독",
                query = "감독 $it"
            )
        })
        persons.addAll(actors.orEmpty().map {
            val cast = if (it.cast.isEmpty()) "출연" else it.cast
            PersonUiModel(
                name = it.peopleNm,
                cast = cast,
                query = "배우 ${it.peopleNm}"
            )
        })
        if (persons.isNotEmpty()) {
            items.add(CastItemUiModel(persons = persons))
        }

        nativeAd?.let {
            items.add(AdUiModel(it))
        }

        val trailers = trailers.orEmpty()
        if (trailers.isNotEmpty()) {
            items.add(TrailerHeaderItemUiModel(movieTitle = title))
            items.addAll(trailers.map {
                TrailerItemUiModel(trailer = it)
            })
            items.add(TrailerFooterItemUiModel(movieTitle = title))
        }
        return ContentUiModel(items)
    }

    fun onFavoriteButtonClick(isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isFavorite) {
                repository.addFavoriteMovie(movie)
                if (movie.isPlan) {
                    repository.insertOpenDateAlarms(OpenDateAlarm(movie.id, movie.title, movie.openDate))
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
            movieDetail = loadDetail(movie)?.also { renderDetail(it) }
        }
    }

    companion object {

        private const val NO_RATING = "평점없음"
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(): DetailViewModel
    }
}
