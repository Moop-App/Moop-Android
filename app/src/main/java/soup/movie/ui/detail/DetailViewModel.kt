package soup.movie.ui.detail

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import soup.movie.data.MoopRepository
import soup.movie.data.model.MovieDetail
import soup.movie.domain.model.screenDays
import soup.movie.ui.EventLiveData
import soup.movie.ui.MutableEventLiveData
import soup.movie.ui.base.BaseViewModel
import soup.movie.ui.home.MovieSelectManager
import soup.movie.util.ImageUriProvider
import soup.movie.util.helper.MM_DD
import soup.movie.util.helper.yesterday
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    repository: MoopRepository,
    private val imageUriProvider: ImageUriProvider
) : BaseViewModel() {

    private val _headerUiModel = MutableLiveData<HeaderUiModel>()
    val headerUiModel: LiveData<HeaderUiModel>
        get() = _headerUiModel

    private val _contentUiModel = MutableLiveData<ContentUiModel>()
    val contentUiModel: LiveData<ContentUiModel>
        get() = _contentUiModel

    private val _shareAction = MutableEventLiveData<ShareAction>()
    val shareAction: EventLiveData<ShareAction>
        get() = _shareAction

    init {
        val movie = MovieSelectManager.getSelectedItem()!!
        _headerUiModel.value = HeaderUiModel(movie)
        repository.getMovieDetail(movie.id)
            .delay(500, TimeUnit.MILLISECONDS)
            .subscribe {
                _headerUiModel.postValue(HeaderUiModel(
                    movie = movie,
                    showTm = it.showTm ?: 0,
                    nations = it.nations.orEmpty(),
                    companys = it.companies.orEmpty()
                ))
                _contentUiModel.postValue(it.toContentUiModel())
            }
            .disposeOnCleared()
    }

    fun requestShareImage(target: ShareTarget, bitmap: Bitmap) {
        imageUriProvider(bitmap)
            .map { ShareAction(target, it, "image/*") }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _shareAction.event = it }
            .disposeOnCleared()
    }

    private fun MovieDetail.toContentUiModel(): ContentUiModel {
        val items = mutableListOf<ContentItemUiModel>()
        items.add(HeaderItemUiModel)
        boxOffice?.run {
            items.add(BoxOfficeItemUiModel(
                rank = rank,
                rankDate = yesterday().MM_DD(),
                audience = audiAcc,
                screenDays = screenDays(),
                rating = naver?.star ?: NO_RATING,
                webLink = naver?.url
            ))
        }
        imdb?.run {
            items.add(ImdbItemUiModel(
                imdb = star,
                rottenTomatoes = rt?.star ?: NO_RATING,
                metascore = mc?.star ?: NO_RATING,
                webLink = url
            ))
        }
        items.add(CgvItemUiModel(
            movieId = cgv?.id.orEmpty(),
            hasInfo = cgv != null,
            rating = cgv?.star ?: NO_RATING
        ))
        items.add(LotteItemUiModel(
            movieId = lotte?.id.orEmpty(),
            hasInfo = lotte != null,
            rating = lotte?.star ?: NO_RATING
        ))
        items.add(MegaboxItemUiModel(
            movieId = megabox?.id.orEmpty(),
            hasInfo = megabox != null,
            rating = megabox?.star ?: NO_RATING
        ))
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
            PersonUiModel(name = it, cast = "감독", query = "감독 $it")
        })
        persons.addAll(actors.orEmpty().map {
            val cast = if (it.cast.isEmpty()) "출연" else it.cast
            PersonUiModel(name = it.peopleNm, cast = cast, query = "배우 ${it.peopleNm}")
        })
        if (persons.isNotEmpty()) {
            items.add(CastItemUiModel(persons = persons))
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

    companion object {

        private const val NO_RATING = "평점없음"
    }
}
