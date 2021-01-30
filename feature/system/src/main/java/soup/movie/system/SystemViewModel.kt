package soup.movie.system

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import soup.movie.ui.EventLiveData
import soup.movie.ui.MutableEventLiveData
import javax.inject.Inject

@HiltViewModel
class SystemViewModel @Inject constructor() : ViewModel() {

    private val _systemEvent = MutableEventLiveData<SystemEvent>()
    val systemEvent: EventLiveData<SystemEvent>
        get() = _systemEvent

    fun openNavigationMenu() {
        _systemEvent.event = SystemEvent.OpenDrawerMenuUiEvent
    }
}
