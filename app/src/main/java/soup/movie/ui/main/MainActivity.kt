package soup.movie.ui.main

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import kotlinx.android.synthetic.main.activity_main.*
import soup.movie.R
import soup.movie.databinding.ActivityMainBinding
import soup.movie.settings.impl.LastMainTabSetting.Tab
import soup.movie.ui.BaseActivity
import soup.movie.ui.helper.FragmentSceneRouter
import soup.movie.ui.helper.FragmentSceneRouter.SceneData
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

    private val fragmentSceneRouter by lazy {
        FragmentSceneRouter(supportFragmentManager, R.id.container)
    }

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
            presenter.setCurrentTab(it.itemId.parseToTabMode())
            true
        }
    }

    override fun render(viewState: MainViewState) {
        printRenderLog { viewState }
        updateSelectedItem(viewState.toItemId())
        fragmentSceneRouter.goTo(viewState.asScene())
    }

    private fun updateSelectedItem(@IdRes itemId: Int) {
        if (bottomNavigation.selectedItemId != itemId) {
            bottomNavigation.selectedItemId = itemId
        }
    }

    @IdRes
    private fun MainViewState.toItemId(): Int = when (this) {
        is NowState -> R.id.action_now
        is PlanState -> (R.id.action_plan)
        is TheatersState -> (R.id.action_theaters)
        is SettingsState -> (R.id.action_settings)
    }

    private fun Int.parseToTabMode(): Tab = when (this) {
        R.id.action_now -> Tab.Now
        R.id.action_plan -> Tab.Plan
        R.id.action_theaters -> Tab.Theaters
        R.id.action_settings -> Tab.Settings
        else -> throw IllegalStateException("0x${toString(16)} is invalid ID")
    }

    private fun MainViewState.asScene(): SceneData = when (this) {
        is NowState -> SceneData(toString()) { NowFragment.newInstance() }
        is PlanState -> SceneData(toString()) { PlanFragment.newInstance() }
        is TheatersState -> SceneData(toString()) { TheatersFragment.newInstance() }
        is SettingsState -> SceneData(toString()) { SettingsFragment.newInstance() }
    }
}
