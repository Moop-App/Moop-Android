package soup.movie.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import dagger.android.support.DaggerFragment

abstract class LegacyBaseFragment<V : LegacyBaseContract.View, P : LegacyBaseContract.Presenter<V>> :
        DaggerFragment(), LegacyBaseContract.View {

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

}
