package soup.movie.ui

import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity<V : BaseContract.View, P : BaseContract.Presenter<V>>
    : DaggerAppCompatActivity() {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    protected abstract val presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        initViewState(this)
    }

    @CallSuper
    protected open fun initViewState(ctx: Context) {
        // stub implementation
    }

    @Suppress("UNCHECKED_CAST")
    override fun onStart() {
        super.onStart()
        presenter.onAttach(this as V)
    }

    override fun onStop() {
        presenter.onDetach()
        super.onStop()
    }

    protected fun commit(@IdRes containerId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(containerId, fragment, fragment.javaClass.simpleName)
                .commit()
    }
}
