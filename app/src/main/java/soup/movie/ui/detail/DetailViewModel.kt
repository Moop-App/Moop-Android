package soup.movie.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import soup.movie.R
import soup.movie.data.MovieSelectManager
import soup.movie.data.model.Movie
import soup.movie.data.model.Theater
import soup.movie.ui.BaseViewModel
import soup.movie.ui.EventLiveData
import soup.movie.ui.EventMutableLiveData
import soup.movie.util.ImageUriProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private var imageUriProvider: ImageUriProvider
): BaseViewModel() {

    private val _viewState = MutableLiveData<DetailViewState>()
    val viewState: LiveData<DetailViewState>
        get() = _viewState

    private val _shareAction = EventMutableLiveData<ShareAction>()
    val shareAction: EventLiveData<ShareAction>
        get() = _shareAction

    init {
        MovieSelectManager
            .asObservable()
            .subscribeOn(Schedulers.io())
            .delay(500, TimeUnit.MILLISECONDS)
            .map { DetailViewState.DoneState(it.toItems()) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _viewState.value = it }
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

    private fun Movie.toItems(): List<DetailViewState.ListItem> {
        val list = mutableListOf(
            DetailViewState.ListItem(Theater.TYPE_CGV, R.layout.detail_item_cgv, this),
            DetailViewState.ListItem(Theater.TYPE_LOTTE, R.layout.detail_item_lotte, this),
            DetailViewState.ListItem(Theater.TYPE_MEGABOX, R.layout.detail_item_megabox, this))
        if (hasNaverInfo()) {
            list.add(DetailViewState.ListItem(Theater.TYPE_NONE, R.layout.detail_item_naver, this))
        }
        val trailers = trailers.orEmpty()
        if (trailers.isNotEmpty()) {
            list.add(DetailViewState.ListItem(Theater.TYPE_NONE, R.layout.detail_item_trailers, this, trailers))
        }
        return list
    }
}
