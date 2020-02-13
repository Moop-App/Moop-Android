package soup.movie.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch
import soup.movie.model.repository.MoopRepository
import soup.movie.ui.EventLiveData
import soup.movie.ui.MutableEventLiveData

class MainViewModel @AssistedInject constructor(
    private val repository: MoopRepository
) : ViewModel() {

    private val _uiEvent = MutableEventLiveData<MainUiEvent>()
    val uiEvent: EventLiveData<MainUiEvent>
        get() = _uiEvent

    fun requestMovie(movieId: String) {
        viewModelScope.launch {
            val movie = repository.findMovie(movieId)
            if (movie != null) {
                _uiEvent.event = MainUiEvent.ShowDetailUiEvent(movie)
            }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(): MainViewModel
    }
}
