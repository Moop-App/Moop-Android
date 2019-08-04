package soup.movie.ui.home

import android.annotation.SuppressLint
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.databinding.HomeContentsBinding
import soup.movie.databinding.HomeFragmentBinding
import soup.movie.databinding.HomeHeaderBinding
import soup.movie.databinding.HomeHeaderHintBinding
import soup.movie.ui.base.BaseFragment
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.ui.home.filter.HomeFilterViewModel
import soup.movie.ui.main.MainViewModel
import soup.movie.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.set

class HomeFragment : BaseFragment(), OnBackPressedListener {

    @Inject
    lateinit var analytics: EventAnalytics

    private lateinit var binding: HomeFragmentBinding
    private lateinit var filterBehavior: BottomSheetBehavior<View>

    private val activityViewModel: MainViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()
    private val filterViewModel: HomeFilterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.filterViewModel = filterViewModel
        binding.init(viewModel)
        binding.initFilter(filterViewModel)
        binding.adaptSystemWindowInset()
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onDestroyView() {
        binding.contents.listView.setOnTouchListener(null)
        super.onDestroyView()
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
                setProgressBackgroundColorSchemeColor(context.getColorCompat(R.color.home_hint))
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
                overScrollMode = View.OVER_SCROLL_NEVER
                setOnTouchListener(HomeListScrollEffect(this))
            }
            errorView.setOnDebounceClickListener {
                viewModel.refresh()
            }
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

    private fun HomeFragmentBinding.initFilter(viewModel: HomeFilterViewModel) {
        filterBehavior = BottomSheetBehavior.from(filter.root).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        filterButton.setOnDebounceClickListener {
            analytics.clickMenuFilter()
            filterBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        viewModel.genreUiModel.observe(viewLifecycleOwner) {
            filter.genreItem.genreFilterGroup.run {
                removeAllViews()
                it.items.forEach {
                    val genreChip: Chip = inflate(context, R.layout.home_filter_item_genre)
                    genreChip.text = it.name
                    genreChip.isChecked = it.isChecked
                    genreChip.setOnCheckedChangeListener { _, isChecked ->
                        viewModel.onGenreFilterClick(it.name, isChecked)
                    }
                    addView(genreChip)
                }
            }
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

    /** Custom Back */

    override fun onBackPressed(): Boolean {
        if (filterBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            filterBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            return true
        }
        return false
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
