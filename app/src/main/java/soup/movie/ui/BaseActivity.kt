package soup.movie.ui

import android.content.Context
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity<V : BaseContract.View, P : BaseContract.Presenter<V>> :
        DaggerAppCompatActivity() {

    protected abstract val binding: ViewDataBinding

    protected abstract val presenter: P

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setLifecycleOwner(this)
        initViewState(this)
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
