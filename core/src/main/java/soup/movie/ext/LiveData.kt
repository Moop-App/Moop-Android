package soup.movie.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import soup.movie.ui.EventLiveData
import soup.movie.ui.EventObserver

inline fun <T> EventLiveData<T>.observeEvent(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    observe(owner, EventObserver { observer(it) })
}

fun <T> MutableLiveData<T>.setValueIfNew(newValue: T) {
    if (this.value != newValue) value = newValue
}

fun <T> MutableLiveData<T>.postValueIfNew(newValue: T) {
    if (this.value != newValue) postValue(newValue)
}
