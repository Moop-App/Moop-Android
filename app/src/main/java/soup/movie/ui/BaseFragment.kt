package soup.movie.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import dagger.android.support.DaggerFragment

abstract class BaseFragment<V : BaseContract.View, P : BaseContract.Presenter<V>> :
        DaggerFragment(), BaseContract.View {

    protected abstract val presenter: P

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

    interface OnBackListener {

        fun onBackPressed(): Boolean
    }
}
