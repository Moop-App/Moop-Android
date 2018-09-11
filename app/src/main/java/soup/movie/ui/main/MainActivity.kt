package soup.movie.ui.main

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import soup.movie.R
import soup.movie.databinding.ActivityMainBinding
import soup.movie.settings.impl.LastMainTabSetting.Tab
import soup.movie.ui.BaseActivity
import soup.movie.ui.main.MainViewState.*
import soup.movie.ui.main.now.NowFragment
import soup.movie.ui.main.plan.PlanFragment
import soup.movie.ui.main.settings.SettingsFragment
import soup.movie.ui.main.theaters.TheatersFragment
import soup.movie.util.delegates.contentView
import soup.movie.util.log.printRenderLog
import javax.inject.Inject

class MainActivity :
        BaseActivity<MainContract.View, MainContract.Presenter>(),
        MainContract.View {

    override val binding by contentView<MainActivity, ActivityMainBinding>(
            R.layout.activity_main
    )

    @Inject
    override lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
    }

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        //TODO: refactor this
        when (intent.action) {
            "soup.movie.action.Nearby" -> {
                presenter.setCurrentTab(Tab.Theaters)
            }
        }
        bottomNavigation.setOnNavigationItemSelectedListener {
            title = it.title
            presenter.setCurrentTab(parseToTabMode(it.itemId))
            true
        }
    }

    override fun render(viewState: MainViewState) {
        viewState.printRenderLog()
        when (viewState) {
            is NowState -> {
                updateSelectedItem(R.id.action_now)
                commit(NowFragment.newInstance())
            }
            is PlanState -> {
                updateSelectedItem(R.id.action_plan)
                commit(PlanFragment.newInstance())
            }
            is TheatersState -> {
                updateSelectedItem(R.id.action_theaters)
                commit(TheatersFragment.newInstance())
            }
            is SettingsState -> {
                updateSelectedItem(R.id.action_settings)
                commit(SettingsFragment.newInstance())
            }
        }
    }

    private fun updateSelectedItem(@IdRes itemId: Int) {
        if (bottomNavigation.selectedItemId != itemId) {
            bottomNavigation.selectedItemId = itemId
        }
    }

    private fun commit(fragment: Fragment) {
        supportFragmentManager.beginTransaction().disallowAddToBackStack()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .commitAllowingStateLoss()
    }

    private fun parseToTabMode(@IdRes itemId: Int): Tab =
            when (itemId) {
                R.id.action_now -> Tab.Now
                R.id.action_plan -> Tab.Plan
                R.id.action_theaters -> Tab.Theaters
                R.id.action_settings -> Tab.Settings
                else -> throw IllegalStateException("Unknown resource ID")
            }
}
