package soup.movie.ui

import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import dagger.android.support.DaggerFragment

abstract class BaseFragment<V : BaseContract.View, P : BaseContract.Presenter<V>>
    : DaggerFragment(), BaseContract.View {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    protected abstract val presenter: P

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    @Suppress("UNCHECKED_CAST")
    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewState(view.context)
        presenter.onAttach(this as V)
    }

    @CallSuper
    protected open fun initViewState(ctx: Context) {
        // stub implementation
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }
}
