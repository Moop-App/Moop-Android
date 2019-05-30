package soup.movie.ui.main

import io.reactivex.android.schedulers.AndroidSchedulers
import soup.movie.data.MoopRepository
import soup.movie.data.model.MovieId
import soup.movie.ui.BaseViewModel
import soup.movie.ui.EventLiveData
import soup.movie.ui.MutableEventLiveData
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: MoopRepository
) : BaseViewModel() {

    private val _uiEvent = MutableEventLiveData<MainUiEvent>()
    val uiEvent: EventLiveData<MainUiEvent>
        get() = _uiEvent

    fun requestMovie(movieId: MovieId) {
        repository.getMovie(movieId)
            .observeOn(AndroidSchedulers.mainThread())
            .map { ShowDetailUiEvent(it) }
            .subscribe { _uiEvent.event = it }
            .disposeOnCleared()
    }
}
