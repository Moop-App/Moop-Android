package soup.movie.ui

import android.content.Context
import android.os.Bundle
import androidx.annotation.CallSuper
import dagger.android.support.DaggerAppCompatActivity
import soup.movie.theme.ThemeBook

abstract class LegacyBaseActivity<V : LegacyBaseContract.View, P : LegacyBaseContract.Presenter<V>> :
        DaggerAppCompatActivity() {

    protected abstract val presenter: P

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeBook.open(this)
        super.onCreate(savedInstanceState)
        setupContentView()
        initViewState(this)
        presenter.onAttach(this as V)
    }

    //TODO: Added for refactoring
    abstract fun setupContentView()

    @CallSuper
    protected open fun initViewState(ctx: Context) {
        // stub implementation
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }
}
