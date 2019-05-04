package soup.movie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

typealias EventLiveData<T> = LiveData<Event<T>>

class EventMutableLiveData<T> : MutableLiveData<Event<T>>() {

    var event: T?
        get() = value?.peekContent()
        set(value) {
            if (value != null) {
                setValue(Event(value))
            }
        }

    fun postEvent(value: T?) {
        if (value != null) {
            postValue(Event(value))
        }
    }
}
