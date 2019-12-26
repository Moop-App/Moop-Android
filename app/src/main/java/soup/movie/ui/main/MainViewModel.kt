package soup.movie.ui.main

import io.reactivex.android.schedulers.AndroidSchedulers
import soup.movie.data.MoopRepository
import soup.movie.ui.EventLiveData
import soup.movie.ui.MutableEventLiveData
import soup.movie.ui.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: MoopRepository
) : BaseViewModel() {

    private val _uiEvent = MutableEventLiveData<MainUiEvent>()
    val uiEvent: EventLiveData<MainUiEvent>
        get() = _uiEvent

    fun requestMovie(movieId: String) {
        repository.getMovie(movieId)
            .observeOn(AndroidSchedulers.mainThread())
            .map { ShowDetailUiEvent(it) }
            .subscribe { _uiEvent.event = it }
            .disposeOnCleared()
    }

    fun openNavigationMenu() {
        _uiEvent.event = OpenDrawerMenuUiEvent
    }
}
