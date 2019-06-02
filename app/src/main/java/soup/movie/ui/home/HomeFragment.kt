package soup.movie.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.home_contents.*
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.databinding.HomeContentsBinding
import soup.movie.databinding.HomeFragmentBinding
import soup.movie.databinding.HomeHeaderBinding
import soup.movie.ui.base.BaseFragment
import soup.movie.ui.main.MainViewModel
import soup.movie.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    private val activityViewModel: MainViewModel by activityViewModel()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition(400, TimeUnit.MILLISECONDS)
        return HomeFragmentBinding.inflate(inflater, container, false)
            .apply {
                header.setup()
                contents.setup()
            }
            .root
    }

    private fun HomeHeaderBinding.setup() {
        toolbar.setNavigationOnClickListener {
            activityViewModel.openNavigationMenu()
        }
        toolbar.inflateMenu(R.menu.fragment_movie_list)
        toolbar.setOnMenuItemClickListener {
            consume {
                if (it.itemId == R.id.action_filter) {
                    analytics.clickMenuFilter()
                    findNavController().navigate(HomeFragmentDirections.actionToFilter())
                }
            }
        }
        actionNow.setOnDebounceClickListener {
            viewModel.onNowClick()
        }
        actionPlan.setOnDebounceClickListener {
            viewModel.onPlanClick()
        }
        viewModel.headerUiModel.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun HomeHeaderBinding.render(uiModel: HomeHeaderUiModel) {
        fun View.isTabChecked(checked: Boolean) {
            isEnabled = checked.not()
            if (this is ViewGroup) {
                children.forEach {
                    it.isEnabled = checked.not()
                }
            }
        }
        actionNow.isTabChecked(uiModel.isNow)
        actionPlan.isTabChecked(uiModel.isNow.not())
    }


    private fun HomeContentsBinding.setup() {
        swipeRefreshLayout.apply {
            setProgressBackgroundColorSchemeColor(context.getColorAttr(R.attr.moop_stageColor))
            setColorSchemeColors(context.getColorAttr(R.attr.moop_stageStarColor))
            setOnRefreshListener {
                viewModel.refresh()
            }
        }
        listView.apply {
            adapter = listAdapter
            itemAnimator = SlideInUpAnimator().apply {
                addDuration = 200
                removeDuration = 200
            }
        }
        errorView.setOnClickListener {
            viewModel.refresh()
        }
        viewModel.contentsUiModel.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun HomeContentsBinding.render(uiModel: HomeContentsUiModel) {
        swipeRefreshLayout.isRefreshing = uiModel.isLoading
        errorView.isVisible = uiModel.isError
        listAdapter.submitList(uiModel.movies)
        noItemsView.isVisible = uiModel.hasNoItem
    }
}
