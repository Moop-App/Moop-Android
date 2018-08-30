package soup.movie.ui

import android.content.Context
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity<V : BaseContract.View, P : BaseContract.Presenter<V>> :
        DaggerAppCompatActivity() {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    protected abstract val presenter: P

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
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

    protected fun commit(@IdRes containerId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(containerId, fragment, fragment.javaClass.simpleName)
                .commit()
    }
}
