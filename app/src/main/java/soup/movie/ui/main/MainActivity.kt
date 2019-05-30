package soup.movie.ui.main

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.view.postOnAnimationDelayed
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.data.MovieSelectManager
import soup.movie.databinding.ActivityMainBinding
import soup.movie.settings.impl.LastMainTabSetting.Tab
import soup.movie.spec.KakaoLink
import soup.movie.ui.BaseActivity
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.helper.FragmentPanelRouter
import soup.movie.ui.helper.FragmentSceneRouter
import soup.movie.ui.helper.FragmentSceneRouter.SceneData
import soup.movie.ui.main.MainUiModel.NowState
import soup.movie.ui.main.MainUiModel.PlanState
import soup.movie.ui.main.home.HomeFragment
import soup.movie.util.Interpolators
import soup.movie.util.observe
import soup.movie.util.observeEvent
import javax.inject.Inject

class MainActivity : BaseActivity(), PanelProvider {

    private val viewModel: MainViewModel by viewModel()

    @Inject
    lateinit var analytics: EventAnalytics

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
            })
            dim.setOnClickListener { state = STATE_HIDDEN }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            lifecycleOwner = this@MainActivity
        }

        //TODO: Improve this please
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        intent?.handleDeepLink()
        initViewState()

        bottomSheetPanel.state = STATE_HIDDEN
        scheduleStartPostponedTransition()

        viewModel.uiModel.observe(this) {
            render(it)
        }
        viewModel.uiEvent.observeEvent(this) {
            execute(it)
        }
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

    private fun initViewState() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            title = it.title
            viewModel.setCurrentTab(it.itemId.parseToTabMode())
            true
        }
        bottomNavigation.setOnNavigationItemReselectedListener {
        }
    }

    private fun Intent.handleDeepLink() {
        when (action) {
            ACTION_VIEW -> {
                KakaoLink.extractMovieId(this)?.let {
                    viewModel.requestMovie(it)
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
        super.onBackPressed()
    }

    private fun render(viewState: MainUiModel) {
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

    private fun execute(action: MainUiEvent) {
        when (action) {
            is ShowDetailUiEvent -> {
                MovieSelectManager.select(action.movie)
                val intent = Intent(this, DetailActivity::class.java)
                startActivityForResult(intent, 0, ActivityOptions
                    .makeSceneTransitionAnimation(this)
                    .toBundle())
            }
        }
    }

    override fun showPanel(panelState: PanelData) {
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

    companion object {

        @IdRes
        private fun MainUiModel.toItemId(): Int = when (this) {
            is NowState -> R.id.action_now
            is PlanState -> R.id.action_plan
        }

        @StringRes
        private fun MainUiModel.toTitleId(): Int = when (this) {
            is NowState -> R.string.tab_now
            is PlanState -> R.string.tab_plan
        }

        private fun Int.parseToTabMode(): Tab = when (this) {
            R.id.action_now -> Tab.Now
            R.id.action_plan -> Tab.Plan
            else -> throw IllegalArgumentException("0x${toString(16)} is invalid ID")
        }

        private fun MainUiModel.asScene(): SceneData {
            return SceneData(toString(), isPersist = false) { HomeFragment.newInstance() }
        }
    }
}
