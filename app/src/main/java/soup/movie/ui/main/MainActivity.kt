package soup.movie.ui.main

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.app.SharedElementCallback
import androidx.core.view.postOnAnimationDelayed
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import kotlinx.android.synthetic.main.activity_main.*
import soup.movie.R
import soup.movie.data.MovieSelectManager
import soup.movie.databinding.ActivityMainBinding
import soup.movie.settings.impl.LastMainTabSetting.Tab
import soup.movie.spec.KakaoLink
import soup.movie.ui.BaseActivity
import soup.movie.ui.detail.DetailActivity
import soup.movie.analytics.EventAnalytics
import soup.movie.ui.helper.FragmentPanelRouter
import soup.movie.ui.helper.FragmentSceneRouter
import soup.movie.ui.helper.FragmentSceneRouter.SceneData
import soup.movie.ui.main.MainActionState.NotFoundAction
import soup.movie.ui.main.MainActionState.ShowDetailAction
import soup.movie.ui.main.MainViewState.*
import soup.movie.ui.main.now.NowFragment
import soup.movie.ui.main.plan.PlanFragment
import soup.movie.ui.main.settings.SettingsFragment
import soup.movie.util.Interpolators
import soup.movie.util.delegates.contentView
import soup.movie.util.showToast
import javax.inject.Inject

class MainActivity :
        BaseActivity<MainContract.View, MainContract.Presenter>(),
        MainContract.View, BaseTabFragment.PanelProvider {

    override val binding by contentView<MainActivity, ActivityMainBinding>(
            R.layout.activity_main
    )

    @Inject
    override lateinit var presenter: MainContract.Presenter

    @Inject
    lateinit var analytics: EventAnalytics

    private var backPressedTime: Long = 0

    private val fragmentSceneRouter by lazy {
        FragmentSceneRouter(supportFragmentManager, R.id.container)
    }

    private val fragmentPanelRouter by lazy {
        FragmentPanelRouter(supportFragmentManager, R.id.bottomSheetContainer)
    }

    private val bottomSheetPanel by lazy {
        BottomSheetBehavior.from(bottomSheet).apply {
            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                private var lastState: Int = STATE_HIDDEN

                @SuppressLint("SwitchIntDef")
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        STATE_HIDDEN -> {
                            fragmentPanelRouter.hide()
                            dim.animateHide()
                        }
                        STATE_SETTLING -> {
                            if (lastState == STATE_HIDDEN) {
                                dim.animateShow()
                            }
                        }
                    }
                    when (newState) {
                        STATE_HIDDEN,
                        STATE_COLLAPSED,
                        STATE_EXPANDED -> lastState = newState
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    bottomNavigation.translationY = bottomNavigation.height * (slideOffset + 1) / 1
                }
            })
            dim.setOnClickListener { state = STATE_HIDDEN }
        }
    }

    private fun View.animateHide() {
        animate().cancel()
        alpha = 1f
        animate()
                .alpha(0f)
                .setDuration(200)
                .setInterpolator(Interpolators.ACCELERATE_DECELERATE)
                .setStartDelay(0)
                .withEndAction { visibility = View.INVISIBLE }
    }

    private fun View.animateShow() {
        animate().cancel()
        alpha = 0f
        visibility = View.VISIBLE
        animate()
                .alpha(1f)
                .setDuration(200)
                .setInterpolator(Interpolators.ACCELERATE_DECELERATE)
                .setStartDelay(0)
                .withEndAction(null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomSheetPanel.state = STATE_HIDDEN
        scheduleStartPostponedTransition()

        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>,
                                             sharedElements: MutableMap<String, View>) {
                fragmentSceneRouter.onInterceptMapSharedElements(names, sharedElements)
            }
        })
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        scheduleStartPostponedTransition()
    }

    private fun scheduleStartPostponedTransition() {
        postponeEnterTransition()

        //FixMe: find a timing to call startPostponedEnterTransition()
        bottomNavigation.postOnAnimationDelayed(100) {
            startPostponedEnterTransition()
        }
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
        when (action) {
            ACTION_VIEW -> {
                KakaoLink.extractMovieId(this).let {
                    presenter.requestMovie(it)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (bottomSheetPanel.state != STATE_HIDDEN) {
            bottomSheetPanel.state = STATE_HIDDEN
            return
        }
        if (fragmentSceneRouter.goBack()) {
            return
        }
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime !in 0..BACK_INTERVAL_TIME) {
            backPressedTime = currentTime
            showToast(R.string.press_back_key_one_more)
            return
        }
        super.onBackPressed()
    }

    override fun render(viewState: MainViewState) {
        analytics.screen(this, viewState.toString())
        setTitle(viewState.toTitleId())
        updateSelectedItem(viewState.toItemId())
        fragmentSceneRouter.goTo(viewState.asScene())
    }

    private fun updateSelectedItem(@IdRes itemId: Int) {
        if (bottomNavigation.selectedItemId != itemId) {
            bottomNavigation.selectedItemId = itemId
        }
    }

    override fun execute(action: MainActionState) {
        when (action) {
            is NotFoundAction -> {
                showToast(R.string.action_detail_unknown)
            }
            is ShowDetailAction -> {
                MovieSelectManager.select(action.movie)
                val intent = Intent(this, DetailActivity::class.java)
                startActivityForResult(intent, 0, ActivityOptions
                        .makeSceneTransitionAnimation(this)
                        .toBundle())
            }
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

    override fun panelIsShown(): Boolean {
        return bottomSheetPanel.state != BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fragmentSceneRouter.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {

        private const val BACK_INTERVAL_TIME: Long = 2000

        @IdRes
        private fun MainViewState.toItemId(): Int = when (this) {
            is NowState -> R.id.action_now
            is PlanState -> R.id.action_plan
            is SettingsState -> R.id.action_settings
        }

        @StringRes
        private fun MainViewState.toTitleId(): Int = when (this) {
            is NowState -> R.string.tab_now
            is PlanState -> R.string.tab_plan
            is SettingsState -> R.string.tab_settings
        }

        private fun Int.parseToTabMode(): Tab = when (this) {
            R.id.action_now -> Tab.Now
            R.id.action_plan -> Tab.Plan
            R.id.action_settings -> Tab.Settings
            else -> throw IllegalArgumentException("0x${toString(16)} is invalid ID")
        }

        private fun MainViewState.asScene(): SceneData = when (this) {
            is NowState -> SceneData(toString(), isPersist = false) { NowFragment.newInstance() }
            is PlanState -> SceneData(toString(), isPersist = false) { PlanFragment.newInstance() }
            is SettingsState -> SceneData(toString()) { SettingsFragment.newInstance() }
        }
    }
}
