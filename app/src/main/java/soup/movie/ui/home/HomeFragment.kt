package soup.movie.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.app.SharedElementCallback
import androidx.core.view.postDelayed
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.setupWithViewPager2
import soup.movie.R
import soup.movie.analytics.EventAnalytics
import soup.movie.databinding.HomeFragmentBinding
import soup.movie.databinding.HomeHeaderHintBinding
import soup.movie.ui.base.BaseFragment
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.ui.base.consumeBackEvent
import soup.movie.ui.home.filter.HomeFilterFragment
import soup.movie.ui.main.MainViewModel
import soup.movie.util.Interpolators
import soup.movie.util.doOnApplyWindowInsets
import soup.movie.util.observe
import soup.movie.util.setOnDebounceClickListener
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeFragment : BaseFragment(), OnBackPressedListener {

    @Inject
    lateinit var analytics: EventAnalytics

    private lateinit var binding: HomeFragmentBinding
    private lateinit var pageAdapter: HomePageAdapter
    private lateinit var filterBehavior: BottomSheetBehavior<FrameLayout>

    private val activityViewModel: MainViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()

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
        binding.init(viewModel)
        binding.adaptSystemWindowInset()
        return binding.root
    }

    private fun HomeFragmentBinding.adaptSystemWindowInset() {
        val fabMargin: Int = root.context.resources.getDimensionPixelSize(R.dimen.fab_margin)
        homeScene.doOnApplyWindowInsets { _, windowInsets, initialPadding ->
            val statusBarTopMargin = initialPadding.top + windowInsets.systemWindowInsetTop
            headerHint.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = statusBarTopMargin
            }
            header.collapsingToolbar.updatePadding(top = statusBarTopMargin)
            filterContainerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = statusBarTopMargin
            }
            filterButton.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = fabMargin + windowInsets.systemWindowInsetBottom
            }
        }
    }

    private fun HomeFragmentBinding.init(viewModel: HomeViewModel) {
//        prepareSharedElements()
        pageAdapter = HomePageAdapter(this@HomeFragment)
        viewPager.adapter = pageAdapter
        header.apply {
            toolbar.setNavigationOnClickListener {
                activityViewModel.openNavigationMenu()
            }
            tabs.setupWithViewPager2(viewPager, autoRefresh = true) { tab, position ->
                when (position) {
                    0 -> {
                        tab.setIcon(R.drawable.asld_home_now)
                        tab.setText(R.string.menu_now)
                    }
                    1 -> {
                        tab.setIcon(R.drawable.asld_home_plan)
                        tab.setText(R.string.menu_plan)
                    }
                    2 -> {
                        tab.setIcon(R.drawable.asld_home_favorite)
                        tab.setText(R.string.menu_favorite)
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
        header.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab) {
                pageAdapter.scrollToTop(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
            }
        })
        viewPager.registerOnPageChangeCallback(pageChangeCallback)

        filterBehavior = BottomSheetBehavior.from(filter).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        filterButton.setOnDebounceClickListener {
            analytics.clickMenuFilter()

            val tag = "filter"
            if (childFragmentManager.findFragmentByTag(tag) == null) {
                childFragmentManager.commit {
                    replace<HomeFilterFragment>(R.id.filter_container_view, tag = tag)
                }
                it.postDelayed(200) {
                    filterBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            } else {
                filterBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    override fun onDestroyView() {
        binding.header.tabs.clearOnTabSelectedListeners()
        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        super.onDestroyView()
    }

    /** UI Renderer */

    private fun HomeHeaderHintBinding.render(uiModel: HomeHeaderUiModel) {
        hintLabel.setText(if (uiModel.isNow) {
            R.string.menu_now
        } else {
            R.string.menu_plan
        })
        hintLabel.apply {
            scaleX = 1.2f
            scaleY = 1.2f
            animate()
                .setDuration(100)
                .setInterpolator(Interpolators.ACCELERATE_DECELERATE)
                .scaleX(1f)
                .scaleY(1f)
        }
    }

    /** Custom Back */

    override fun onBackPressed(): Boolean {
        if (filterBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            filterBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            return true
        }
        val currentPosition = binding.viewPager.currentItem
        val current = pageAdapter.getFragment(currentPosition)
        if (current.consumeBackEvent()) {
            binding.header.appBar.setExpanded(true, true)
            return true
        }
        if (currentPosition > 0) {
            binding.viewPager.currentItem = 0
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
