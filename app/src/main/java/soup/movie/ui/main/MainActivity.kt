package soup.movie.ui.main

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_main.*
import soup.movie.R
import soup.movie.settings.impl.MainTabSetting
import soup.movie.ui.BaseActivity
import soup.movie.ui.main.MainViewState.*
import soup.movie.ui.main.now.NowFragment
import soup.movie.ui.main.plan.PlanFragment
import soup.movie.ui.main.settings.SettingsFragment
import timber.log.Timber
import javax.inject.Inject

class MainActivity :
        BaseActivity<MainContract.View, MainContract.Presenter>(),
        MainContract.View {

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
        BottomNavigationViewHelper.disableShiftMode(bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener {
            title = it.title
            presenter.setCurrentTab(parseToTabMode(it.itemId))
            true
        }
    }

    override fun render(viewState: MainViewState) {
        Timber.d("render: %s", viewState)
        when (viewState) {
            is NowState -> {
                bottomNavigation.selectedItemId = R.id.action_now
                commit(NowFragment.newInstance())
            }
            is PlanState -> {
                bottomNavigation.selectedItemId = R.id.action_plan
                commit(PlanFragment.newInstance())
            }
            is SettingsState -> {
                bottomNavigation.selectedItemId = R.id.action_settings
                commit(SettingsFragment.newInstance())
            }
        }
    }

    private fun commit(fragment: Fragment) {
        supportFragmentManager.beginTransaction().disallowAddToBackStack()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .commit()
    }

    private fun parseToTabMode(@IdRes itemId: Int): MainTabSetting.Tab =
            when (itemId) {
                R.id.action_now -> MainTabSetting.Tab.Now
                R.id.action_plan -> MainTabSetting.Tab.Plan
                R.id.action_settings -> MainTabSetting.Tab.Settings
                else -> throw IllegalStateException("Unknown resource ID")
            }
}
