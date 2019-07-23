package soup.movie.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.databinding.HomeContentsBinding
import soup.movie.databinding.HomeFragmentBinding
import soup.movie.databinding.HomeHeaderBinding
import soup.movie.databinding.HomeHeaderHintBinding
import soup.movie.ui.base.BaseFragment
import soup.movie.ui.main.MainViewModel
import soup.movie.util.doOnApplyWindowInsets
import soup.movie.util.getColorAttr
import soup.movie.util.observe
import soup.movie.util.setOnDebounceClickListener
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.set

class HomeFragment : BaseFragment() {

    private val activityViewModel: MainViewModel by activityViewModel()
    private val viewModel: HomeViewModel by viewModel()

    @Inject
    lateinit var analytics: EventAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomeFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.init(viewModel)
        binding.adaptSystemWindowInset()
        return binding.root
    }

    private fun HomeFragmentBinding.adaptSystemWindowInset() {
        homeScene.doOnApplyWindowInsets { view, windowInsets, initialPadding ->
            view.updatePadding(
                top = initialPadding.top + windowInsets.systemWindowInsetTop
            )
            contents.listView.updatePadding(
                bottom = initialPadding.bottom + windowInsets.systemWindowInsetBottom
            )
            filterButton.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = windowInsets.systemWindowInsetBottom
            }
        }
    }

    private fun HomeFragmentBinding.init(viewModel: HomeViewModel) {
        prepareSharedElements()
        header.apply {
            toolbar.setNavigationOnClickListener {
                activityViewModel.openNavigationMenu()
            }
            actionNow.setOnDebounceClickListener {
                viewModel.onNowClick()
            }
            actionPlan.setOnDebounceClickListener {
                viewModel.onPlanClick()
            }
        }
        headerHint.hintButton.setOnDebounceClickListener {
            header.appBar.setExpanded(true)
            contents.listView.smoothScrollToPosition(0)
        }

        val listAdapter = HomeListAdapter { movie, sharedElements ->
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
        contents.apply {
            swipeRefreshLayout.apply {
                setProgressBackgroundColorSchemeColor(context.getColorAttr(R.attr.colorSurface))
                setColorSchemeColors(context.getColorAttr(R.attr.colorOnSurface))
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
            errorView.setOnDebounceClickListener {
                viewModel.refresh()
            }
        }
        filterButton.setOnDebounceClickListener {
            analytics.clickMenuFilter()
            findNavController().navigate(HomeFragmentDirections.actionToFilter())
        }
        viewModel.headerUiModel.observe(viewLifecycleOwner) {
            header.render(it)
            headerHint.render(it)
        }
        viewModel.contentsUiModel.observe(viewLifecycleOwner) {
            contents.render(it)
            listAdapter.submitList(it.movies)
        }
    }

    /** UI Renderer */

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
        actionPlan.isTabChecked(uiModel.isPlan)
    }

    private fun HomeHeaderHintBinding.render(uiModel: HomeHeaderUiModel) {
        hintLabel.setText(if (uiModel.isNow) {
            R.string.menu_now
        } else {
            R.string.menu_plan
        })
    }

    private fun HomeContentsBinding.render(uiModel: HomeContentsUiModel) {
        swipeRefreshLayout.isRefreshing = uiModel.isLoading
        errorView.isVisible = uiModel.isError
        noItemsView.isVisible = uiModel.hasNoItem
    }

    /** SharedElements */

    private fun HomeFragmentBinding.prepareSharedElements() {
        postponeEnterTransition(400, TimeUnit.MILLISECONDS)
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: List<String>,
                sharedElements: MutableMap<String, View>
            ) {
                sharedElements.clear()
                MovieSelectManager.getSelectedItem()?.run {
                    contents.listView.findViewWithTag<View>(id)?.let { movieView ->
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
}
