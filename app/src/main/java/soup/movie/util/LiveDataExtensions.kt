package soup.movie.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import soup.movie.ui.EventLiveData
import soup.movie.ui.EventObserver

inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    observe(owner, Observer { observer(it) })
}

inline fun <T> EventLiveData<T>.observeEvent(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    observe(owner, EventObserver { observer(it) })
}

fun <T> MutableLiveData<T>.setValueIfNew(newValue: T) {
    if (this.value != newValue) value = newValue
}

fun <T> MutableLiveData<T>.postValueIfNew(newValue: T) {
    if (this.value != newValue) postValue(newValue)
}
