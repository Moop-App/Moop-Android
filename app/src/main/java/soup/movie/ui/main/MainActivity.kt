package soup.movie.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import kotlinx.android.synthetic.main.activity_main.*
import soup.movie.R
import soup.movie.data.helper.saveTo
import soup.movie.data.model.Movie
import soup.movie.databinding.ActivityMainBinding
import soup.movie.settings.impl.LastMainTabSetting.Tab
import soup.movie.ui.BaseActivity
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.helper.FragmentPanelRouter
import soup.movie.ui.helper.FragmentSceneRouter
import soup.movie.ui.helper.FragmentSceneRouter.SceneData
import soup.movie.ui.main.MainViewState.*
import soup.movie.ui.main.now.NowFragment
import soup.movie.ui.main.plan.PlanFragment
import soup.movie.ui.main.settings.SettingsFragment
import soup.movie.ui.main.theaters.TheatersFragment
import soup.movie.util.animateHide
import soup.movie.util.animateShow
import soup.movie.util.delegates.contentView
import soup.movie.util.log.printRenderLog
import timber.log.Timber
import javax.inject.Inject

class MainActivity :
        BaseActivity<MainContract.View, MainContract.Presenter>(),
        MainContract.View, BaseTabFragment.PanelProvider {

    override val binding by contentView<MainActivity, ActivityMainBinding>(
            R.layout.activity_main
    )

    @Inject
    override lateinit var presenter: MainContract.Presenter

    private val fragmentSceneRouter by lazy {
        FragmentSceneRouter(supportFragmentManager, R.id.container)
    }

    private val fragmentPanelRouter by lazy {
        FragmentPanelRouter(supportFragmentManager, R.id.bottomSheetContainer)
    }

    private val bottomSheetPanel by lazy {
        BottomSheetBehavior.from(bottomSheet).apply {
            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                @SuppressLint("SwitchIntDef")
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        STATE_HIDDEN -> {
                            fragmentPanelRouter.hide()
                            dim.animateHide(true)
                        }
                        STATE_COLLAPSED -> {
                            dim.animateShow(true)
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    bottomNavigation.translationY = bottomNavigation.height * (slideOffset + 1) / 1
                }
            })
            dim.setOnClickListener { state = STATE_HIDDEN }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        bottomSheetPanel.state = STATE_HIDDEN
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
        bottomNavigation.setOnNavigationItemReselectedListener {
            fragmentSceneRouter.reselectTab()
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
        if (bottomSheetPanel.state != STATE_HIDDEN) {
            bottomSheetPanel.state = STATE_HIDDEN
            return
        }
        if (fragmentSceneRouter.goBack().not()) {
            super.onBackPressed()
        }
    }

    override fun render(viewState: MainViewState) {
        printRenderLog { viewState }
        setTitle(viewState.toTitleId())
        updateSelectedItem(viewState.toItemId())
        fragmentSceneRouter.goTo(viewState.asScene())
    }

    private fun updateSelectedItem(@IdRes itemId: Int) {
        if (bottomNavigation.selectedItemId != itemId) {
            bottomNavigation.selectedItemId = itemId
        }
    }

    override fun showPanel(panelState: BaseTabFragment.PanelData) {
        bottomSheetPanel.state = BottomSheetBehavior.STATE_COLLAPSED
        fragmentPanelRouter.show(panelState)
    }

    override fun hidePanel() {
        bottomSheetPanel.state = BottomSheetBehavior.STATE_HIDDEN
        fragmentPanelRouter.hide()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fragmentSceneRouter.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {

        private const val ACTION_SHOW_TAB = "soup.movie.action.SHOW_TAB"
        private const val EXTRA_TAB = "tab"
        private const val EXTRA_TAB_THEATERS = "theaters"

        @IdRes
        private fun MainViewState.toItemId(): Int = when (this) {
            is NowState -> R.id.action_now
            is PlanState -> R.id.action_plan
            is TheatersState -> R.id.action_theaters
            is SettingsState -> R.id.action_settings
        }

        @StringRes
        private fun MainViewState.toTitleId(): Int = when (this) {
            is NowState -> R.string.tab_now
            is PlanState -> R.string.tab_plan
            is TheatersState -> R.string.tab_theaters
            is SettingsState -> R.string.tab_settings
        }

        private fun Int.parseToTabMode(): Tab = when (this) {
            R.id.action_now -> Tab.Now
            R.id.action_plan -> Tab.Plan
            R.id.action_theaters -> Tab.Theaters
            R.id.action_settings -> Tab.Settings
            else -> throw IllegalArgumentException("0x${toString(16)} is invalid ID")
        }

        private fun MainViewState.asScene(): SceneData = when (this) {
            is NowState -> SceneData(toString(), isPersist = false) { NowFragment.newInstance() }
            is PlanState -> SceneData(toString(), isPersist = false) { PlanFragment.newInstance() }
            is TheatersState -> SceneData(toString(), enter = 0, exit = R.anim.persist) { TheatersFragment.newInstance() }
            is SettingsState -> SceneData(toString()) { SettingsFragment.newInstance() }
        }
    }
}
