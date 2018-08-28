package soup.movie.ui.main

import android.content.Context
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.internal.BottomNavigationViewHelper
import android.support.design.widget.BottomNavigationView
import butterknife.BindView
import soup.movie.R
import soup.movie.ui.BaseActivity
import soup.movie.ui.main.MainViewState.*
import soup.movie.ui.main.now.NowFragment
import soup.movie.ui.main.plan.PlanFragment
import soup.movie.ui.main.settings.SettingsFragment
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity<MainContract.View, MainContract.Presenter>(), MainContract.View {

    @BindView(R.id.bottom_navigation)
    internal lateinit var navigationView: BottomNavigationView

    @Inject
    override lateinit var presenter: MainContract.Presenter

    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
    }

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        BottomNavigationViewHelper.disableShiftMode(navigationView)
        navigationView.setOnNavigationItemSelectedListener {
            title = it.title
            presenter.setTabMode(parseToTabMode(it.itemId))
            true
        }
        navigationView.selectedItemId = R.id.action_now
    }

    override fun render(viewState: MainViewState) {
        Timber.d("render: %s", viewState)
        when (viewState) {
            is NowState -> renderNowState()
            is PlanState -> renderPlanState()
            is SettingsState -> renderSettingsState()
        }
    }

    private fun renderNowState() {
        commit(R.id.container_tab, NowFragment.newInstance())
    }

    private fun renderPlanState() {
        commit(R.id.container_tab, PlanFragment.newInstance())
    }

    private fun renderSettingsState() {
        commit(R.id.container_tab, SettingsFragment.newInstance())
    }

    @MainContract.TabMode
    private fun parseToTabMode(@IdRes itemId: Int): Int = when (itemId) {
        R.id.action_now -> MainContract.TAB_MODE_NOW
        R.id.action_plan -> MainContract.TAB_MODE_PLAN
        R.id.action_settings -> MainContract.TAB_MODE_SETTINGS
        else -> throw IllegalStateException("Unknown resource ID")
    }
}
