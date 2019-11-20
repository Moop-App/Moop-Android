package soup.movie.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.setupWithViewPager2
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.databinding.HomeFragmentBinding
import soup.movie.databinding.HomeHeaderHintBinding
import soup.movie.ui.base.BaseFragment
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.ui.home.filter.HomeFilterViewModel
import soup.movie.ui.home.now.HomeNowFragment
import soup.movie.ui.home.plan.HomePlanFragment
import soup.movie.ui.main.MainViewModel
import soup.movie.util.doOnApplyWindowInsets
import soup.movie.util.inflate
import soup.movie.util.observe
import soup.movie.util.setOnDebounceClickListener
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeFragment : BaseFragment(), OnBackPressedListener {

    @Inject
    lateinit var analytics: EventAnalytics

    private lateinit var binding: HomeFragmentBinding
    private lateinit var filterBehavior: BottomSheetBehavior<View>

    private val activityViewModel: MainViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()
    private val filterViewModel: HomeFilterViewModel by viewModels()

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            val isNow = position == 0
            if (isNow) {
                viewModel.onNowTabClick()
            } else {
                viewModel.onPlanTabClick()
            }
        }
    }

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

    private fun HomeFragmentBinding.adaptSystemWindowInset() {
        val fabMargin: Int = root.context.resources.getDimensionPixelSize(R.dimen.fab_margin)
        homeScene.doOnApplyWindowInsets { view, windowInsets, initialPadding ->
            view.updatePadding(
                top = initialPadding.top + windowInsets.systemWindowInsetTop
            )
            filterButton.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = fabMargin + windowInsets.systemWindowInsetBottom
            }
        }
    }

    private fun HomeFragmentBinding.init(viewModel: HomeViewModel) {
//        prepareSharedElements()

        val pageAdapter = object : FragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle) {

            private val items= arrayOf<Fragment>(
                HomeNowFragment(),
                HomePlanFragment()
            )

            override fun createFragment(position: Int): Fragment = items[position]

            override fun getItemCount(): Int = items.size

            fun scrollToTop(position: Int) {
                val item = items.getOrNull(position)
                if (item is HomeTabFragment) {
                    item.scrollToTop()
                }
            }
        }
        viewPager.offscreenPageLimit = pageAdapter.itemCount
        viewPager.adapter = pageAdapter
        header.apply {
            toolbar.setNavigationOnClickListener {
                activityViewModel.openNavigationMenu()
            }
            tabs.setupWithViewPager2(viewPager, autoRefresh = true) { tab, position ->
                when (position) {
                    0 -> {
                        tab.setIcon(R.drawable.ic_round_movie)
                        tab.setText(R.string.menu_now)
                    }
                    1 -> {
                        tab.setIcon(R.drawable.ic_round_plan)
                        tab.setText(R.string.menu_plan)
                    }
                }
            }
        }
        headerHint.hintButton.setOnDebounceClickListener {
            header.appBar.setExpanded(true)
            pageAdapter.scrollToTop(viewPager.currentItem)
        }
        viewModel.headerUiModel.observe(viewLifecycleOwner) {
            headerHint.render(it)
        }
        binding.header.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab) {
                pageAdapter.scrollToTop(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
            }
        })
        viewPager.registerOnPageChangeCallback(pageChangeCallback)
    }

    override fun onDestroyView() {
        binding.header.tabs.clearOnTabSelectedListeners()
        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        super.onDestroyView()
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

    private fun HomeHeaderHintBinding.render(uiModel: HomeHeaderUiModel) {
        hintLabel.setText(if (uiModel.isNow) {
            R.string.menu_now
        } else {
            R.string.menu_plan
        })
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
//                    contents.listView.findViewWithTag<View>(id)?.let { movieView ->
//                        names.forEach { name ->
//                            val id: Int = when (name) {
//                                "background" -> R.id.backgroundView
//                                "poster" -> R.id.posterView
//                                "age_bg" -> R.id.ageBgView
//                                "new" -> R.id.newView
//                                "best" -> R.id.bestView
//                                "d_day" -> R.id.dDayView
//                                else -> View.NO_ID
//                            }
//                            movieView.findViewById<View>(id)?.let {
//                                sharedElements[name] = it
//                            }
//                        }
//                    }
                }
            }
        })
    }
}
