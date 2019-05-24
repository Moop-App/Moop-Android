package soup.movie.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import soup.movie.data.MovieSelectManager
import soup.movie.data.model.Movie
import soup.movie.ui.BaseViewModel
import soup.movie.ui.EventLiveData
import soup.movie.ui.MutableEventLiveData
import soup.movie.util.ImageUriProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DetailViewModel @Inject constructor(
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
        _headerUiModel.value = HeaderUiModel(
            movie = MovieSelectManager.getSelectedItem()!!
        )
        MovieSelectManager
            .asObservable()
            .subscribeOn(Schedulers.io())
            .delay(500, TimeUnit.MILLISECONDS)
            .map { it.toContentUiModel() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _contentUiModel.value = it }
            .disposeOnCleared()
    }

    fun requestShareImage(url: String) {
        imageUriProvider(url)
            .map { ShareAction(it, getImageMimeType(url)) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _shareAction.event = it }
            .disposeOnCleared()
    }

    private fun getImageMimeType(fileName: String): String = when {
        fileName.endsWith(".png") -> "image/png"
        fileName.endsWith(".gif") -> "image/gif"
        else -> "image/jpeg"
    }

    private fun Movie.toContentUiModel(): ContentUiModel {
        val items = mutableListOf<ContentItemUiModel>()
        val genreList = genre.orEmpty()
        if (genreList.isNotEmpty()) {
            items.add(GenreItemUiModel(
                genreList = genreList
            ))
        }
        items.add(CgvItemUiModel(
            movieId = cgv?.id.orEmpty(),
            hasInfo = cgv != null,
            rating = cgv?.egg ?: "-"
        ))
        items.add(LotteItemUiModel(
            movieId = lotte?.id.orEmpty(),
            hasInfo = lotte != null,
            rating = lotte?.star ?: "-"
        ))
        items.add(MegaboxItemUiModel(
            movieId = megabox?.id.orEmpty(),
            hasInfo = megabox != null,
            rating = megabox?.star ?: "-"
        ))
        if (hasNaverInfo()) {
            items.add(NaverItemUiModel(
                rating = naver?.userRating ?: "-",
                webLink = naver?.link
            ))
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
}
