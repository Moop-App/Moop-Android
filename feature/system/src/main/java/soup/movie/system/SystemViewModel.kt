package soup.movie.system

import androidx.lifecycle.ViewModel
import com.squareup.inject.assisted.AssistedInject
import soup.movie.ui.EventLiveData
import soup.movie.ui.MutableEventLiveData

class SystemViewModel @AssistedInject constructor() : ViewModel() {

    private val _systemEvent = MutableEventLiveData<SystemEvent>()
    val systemEvent: EventLiveData<SystemEvent>
        get() = _systemEvent

    fun openNavigationMenu() {
        _systemEvent.event = SystemEvent.OpenDrawerMenuUiEvent
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(): SystemViewModel
    }
}
