package soup.movie.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import soup.movie.ui.ResultMapper

abstract class BaseViewModel : ViewModel(), ResultMapper {

    private val disposable = CompositeDisposable()

    protected fun Disposable.disposeOnCleared() {
        disposable.add(this)
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}
