package soup.movie.ui.main.home

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.app.SharedElementCallback
import androidx.core.view.isVisible
import androidx.core.view.postOnAnimationDelayed
import com.google.android.material.bottomsheet.BottomSheetBehavior
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.home_fragment.*
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.data.MovieSelectManager
import soup.movie.databinding.HomeFragmentBinding
import soup.movie.ui.BaseFragment
import soup.movie.ui.detail.DetailActivity
import soup.movie.ui.helper.FragmentPanelRouter
import soup.movie.ui.main.home.filter.HomeFilterFragment
import soup.movie.ui.search.SearchActivity
import soup.movie.ui.settings.SettingsActivity
import soup.movie.util.*
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    private val viewModel: HomeViewModel by viewModel()

    @Inject
    lateinit var analytics: EventAnalytics

    private val listAdapter by lazy {
        HomeListAdapter { movie, sharedElements ->
            analytics.clickMovie(isNow = movie.isNow)
            MovieSelectManager.select(movie)
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            startActivityForResult(intent, 0, ActivityOptions
                .makeSceneTransitionAnimation(requireActivity(), *sharedElements)
                .toBundle())
        }
    }

    private val fragmentPanelRouter by lazyFast {
        FragmentPanelRouter(childFragmentManager, R.id.bottomSheetContainer)
    }

    private val bottomSheetPanel by lazyFast {
        BottomSheetBehavior.from(bottomSheet).apply {
            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                private var lastState: Int = BottomSheetBehavior.STATE_HIDDEN

                @SuppressLint("SwitchIntDef")
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            fragmentPanelRouter.hide()
                            dim.animateHide()
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
                            if (lastState == BottomSheetBehavior.STATE_HIDDEN) {
                                dim.animateShow()
                            }
                        }
                    }
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN,
                        BottomSheetBehavior.STATE_COLLAPSED,
                        BottomSheetBehavior.STATE_EXPANDED -> lastState = newState
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
            dim.setOnClickListener { state = BottomSheetBehavior.STATE_HIDDEN }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: List<String>,
                sharedElements: MutableMap<String, View>
            ) {
                sharedElements.clear()
                MovieSelectManager.getSelectedItem()?.run {
                    listView.findViewWithTag<View>(id)?.let { movieView ->
                        names.forEach { name ->
                            val id: Int = when (name) {
                                "background" -> R.id.backgroundView
                                "poster" -> R.id.posterView
                                "age_bg" -> R.id.ageBgView
                                "new" -> R.id.newView
                                "best" -> R.id.bestView
                                "d_day" -> R.id.dDayView
                                else -> View.NO_ID
                            }
                            movieView.findViewById<View>(id)?.let {
                                sharedElements[name] = it
                            }
                        }
                    }
                }
            }
        })
    }

    private fun scheduleStartPostponedTransition() {
        postponeEnterTransition()

        //FixMe: find a timing to call startPostponedEnterTransition()
        bottomNavigation.postOnAnimationDelayed(100) {
            startPostponedEnterTransition()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_movie_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                val intent = Intent(requireActivity(), SearchActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_filter -> {
                if (panelIsShown()) {
                    hidePanel()
                } else {
                    analytics.clickMenuFilter()
                    showPanel(HomeFilterFragment.toPanelData())
                }
                return true
            }
            R.id.action_settings -> {
                val intent = Intent(requireActivity(), SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return HomeFragmentBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scheduleStartPostponedTransition()
        initViewState(view.context)
        viewModel.uiModel.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun initViewState(ctx: Context) {
        bottomSheetPanel.state = BottomSheetBehavior.STATE_HIDDEN
        listView.apply {
            adapter = listAdapter
            itemAnimator = SlideInUpAnimator().apply {
                addDuration = 200
                removeDuration = 200
            }
        }
        swipeRefreshLayout.apply {
            setProgressBackgroundColorSchemeColor(ctx.getColorAttr(R.attr.moop_stageColor))
            setColorSchemeColors(ctx.getColorAttr(R.attr.moop_stageStarColor))
            setOnRefreshListener {
                viewModel.refresh()
            }
        }
        errorView.setOnClickListener {
            viewModel.refresh()
        }
        bottomNavigation.setOnNavigationItemSelectedListener {
            consume {
                when (it.itemId) {
                    R.id.action_now -> {
                        activity?.setTitle(R.string.tab_now)
                        viewModel.onNowClick()
                    }
                    R.id.action_plan -> {
                        activity?.setTitle(R.string.tab_plan)
                        viewModel.onPlanClick()
                    }
                }
            }
        }
    }

    private fun render(viewState: HomeUiModel) {
        swipeRefreshLayout?.isRefreshing = viewState is HomeUiModel.LoadingState
        errorView?.isVisible = viewState is HomeUiModel.ErrorState
        noItemsView?.isVisible = viewState.hasNoItems()
        if (viewState is HomeUiModel.DoneState) {
            listAdapter.submitList(viewState.movies)
        }
    }

    private fun showPanel(panelState: PanelData) {
        bottomSheetPanel.state = BottomSheetBehavior.STATE_COLLAPSED
        fragmentPanelRouter.show(panelState)
    }

    private fun hidePanel() {
        bottomSheetPanel.state = BottomSheetBehavior.STATE_HIDDEN
        fragmentPanelRouter.hide()
    }

    private fun panelIsShown(): Boolean {
        return bottomSheetPanel.state != BottomSheetBehavior.STATE_HIDDEN
    }

    companion object {

        fun newInstance(): HomeFragment = HomeFragment()
    }
}
