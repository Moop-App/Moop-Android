package soup.movie.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import soup.movie.ads.AdsManager
import soup.movie.model.repository.MoopRepository
import soup.movie.ui.EventLiveData
import soup.movie.ui.MutableEventLiveData

class MainViewModel @AssistedInject constructor(
    private val repository: MoopRepository,
    adsManager: AdsManager
) : ViewModel() {

    private val _uiEvent = MutableEventLiveData<MainUiEvent>()
    val uiEvent: EventLiveData<MainUiEvent>
        get() = _uiEvent

    init {
        viewModelScope.launch(Dispatchers.IO) {
            adsManager.loadNextNativeAd()
        }
    }

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
