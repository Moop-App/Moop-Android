package soup.movie.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.core.view.isVisible
import androidx.core.view.postOnAnimationDelayed
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.home_fragment.*
import soup.movie.MainDirections
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.databinding.HomeFragmentBinding
import soup.movie.ui.base.BaseFragment
import soup.movie.util.consume
import soup.movie.util.getColorAttr
import soup.movie.util.lazyFast
import soup.movie.util.observe
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    private val viewModel: HomeViewModel by viewModel()

    @Inject
    lateinit var analytics: EventAnalytics

    private val listAdapter by lazyFast {
        HomeListAdapter { movie, sharedElements ->
            analytics.clickMovie()
            MovieSelectManager.select(movie)
            findNavController().navigate(
                HomeFragmentDirections.actionToDetail(),
                ActivityNavigatorExtras(
                    activityOptions = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(requireActivity(), *sharedElements)
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                findNavController().navigate(MainDirections.actionToSearch())
                return true
            }
            R.id.action_filter -> {
                analytics.clickMenuFilter()
                findNavController().navigate(HomeFragmentDirections.actionToFilter())
                return true
            }
            R.id.action_settings -> {
                findNavController().navigate(MainDirections.actionToSettings())
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
        toolbar.inflateMenu(R.menu.fragment_movie_list)
        toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
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
                    R.id.action_now -> viewModel.onNowClick()
                    R.id.action_plan -> viewModel.onPlanClick()
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
}
