package soup.movie.ui

import androidx.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.ui.BaseContract.Presenter
import soup.movie.ui.BaseContract.View

abstract class BasePresenter<V : View> : Presenter<V> {

    protected var view: V? = null

    private var subscriptions: CompositeDisposable? = null

    override fun onAttach(view: V) {
        this.view = view
        subscriptions = CompositeDisposable()
                .also { initObservable(it) }
    }

    override fun onDetach() {
        subscriptions?.dispose()
        subscriptions = null
        this.view = null
    }

    @CallSuper
    protected open fun initObservable(disposable: DisposableContainer) {
        //stub implementation
    }
}
