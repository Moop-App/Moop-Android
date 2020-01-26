package soup.movie.ui.main

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import soup.movie.data.repository.MoopRepository
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
        viewModelScope.launch {
            val movie = repository.findMovie(movieId)
            if (movie != null) {
                _uiEvent.event = ShowDetailUiEvent(movie)
            }
        }
    }

    fun openNavigationMenu() {
        _uiEvent.event = OpenDrawerMenuUiEvent
    }
}
