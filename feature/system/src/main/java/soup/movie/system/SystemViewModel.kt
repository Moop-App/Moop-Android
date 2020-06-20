package soup.movie.system

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import soup.movie.ui.EventLiveData
import soup.movie.ui.MutableEventLiveData

class SystemViewModel @ViewModelInject constructor() : ViewModel() {

    private val _systemEvent = MutableEventLiveData<SystemEvent>()
    val systemEvent: EventLiveData<SystemEvent>
        get() = _systemEvent

    fun openNavigationMenu() {
        _systemEvent.event = SystemEvent.OpenDrawerMenuUiEvent
    }
}
