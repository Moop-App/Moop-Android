package soup.movie.ui.main

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.os.Bundle
import androidx.annotation.IdRes
import kotlinx.android.synthetic.main.activity_main.*
import soup.movie.R
import soup.movie.data.helper.saveTo
import soup.movie.data.model.Movie
import soup.movie.databinding.ActivityMainBinding
import soup.movie.settings.impl.LastMainTabSetting.Tab
import soup.movie.ui.BaseActivity
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.helper.FragmentSceneRouter
import soup.movie.ui.helper.FragmentSceneRouter.SceneData
import soup.movie.ui.main.MainViewState.*
import soup.movie.ui.main.now.NowFragment
import soup.movie.ui.main.plan.PlanFragment
import soup.movie.ui.main.settings.SettingsFragment
import soup.movie.ui.main.theaters.TheatersFragment
import soup.movie.util.delegates.contentView
import soup.movie.util.log.printRenderLog
import timber.log.Timber
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.handleDeepLink()
    }

    override fun initViewState(ctx: Context) {
        super.initViewState(ctx)
        intent?.handleDeepLink()
        bottomNavigation.setOnNavigationItemSelectedListener {
            title = it.title
            presenter.setCurrentTab(it.itemId.parseToTabMode())
            true
        }
    }

    private fun Intent.handleDeepLink() {
        Timber.d("SOUP %s", this)
        when (action) {
            ACTION_SHOW_TAB -> {
                when (getStringExtra(EXTRA_TAB)) {
                    EXTRA_TAB_THEATERS -> presenter.setCurrentTab(Tab.Theaters)
                }
                removeExtra(EXTRA_TAB)
            }
            ACTION_VIEW -> {
                data?.getQueryParameter("id")?.let {
                    presenter.requestMovie(it)
                }
            }
        }
    }

    override fun showMovieDetail(movie: Movie) {
        Intent(this, DetailActivity::class.java).also {
            movie.saveTo(it)
            startActivity(it)
        }
    }

    override fun onBackPressed() {
        if (fragmentSceneRouter.goBack().not()) {
            super.onBackPressed()
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
        is NowState -> SceneData(toString(), isPersist = false) { NowFragment.newInstance() }
        is PlanState -> SceneData(toString(), isPersist = false) { PlanFragment.newInstance() }
        is TheatersState -> SceneData(toString()) { TheatersFragment.newInstance() }
        is SettingsState -> SceneData(toString()) { SettingsFragment.newInstance() }
    }

    companion object {

        private const val ACTION_SHOW_TAB = "soup.movie.action.SHOW_TAB"
        private const val EXTRA_TAB = "tab"
        private const val EXTRA_TAB_THEATERS = "theaters"
    }
}
