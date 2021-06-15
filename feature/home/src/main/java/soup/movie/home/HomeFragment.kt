/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.isInvisible
import androidx.core.view.postDelayed
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.setupWithViewPager2
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import soup.movie.analytics.EventAnalytics
import soup.movie.home.databinding.HomeFragmentBinding
import soup.movie.home.databinding.HomeHeaderHintBinding
import soup.movie.home.filter.HomeFilterFragment
import soup.movie.system.SystemViewModel
import soup.movie.ui.base.OnBackPressedListener
import soup.movie.ui.base.consumeBackEvent
import soup.movie.util.Interpolators
import soup.movie.util.autoCleared
import soup.movie.util.setOnDebounceClickListener
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment), OnBackPressedListener {

    @Inject
    lateinit var analytics: EventAnalytics

    private var binding: HomeFragmentBinding by autoCleared {
        header.tabs.clearOnTabSelectedListeners()
        viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
    }
    private lateinit var pageAdapter: HomePageAdapter
    private lateinit var filterBehavior: BottomSheetBehavior<FrameLayout>

    private val systemViewModel: SystemViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            when (position) {
                0 -> viewModel.onNowTabClick()
                1 -> viewModel.onPlanTabClick()
                2 -> viewModel.onFavoriteTabClick()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeFragmentBinding.bind(view).apply {
            initViewState(viewModel)
            adaptSystemWindowInset()
        }
    }

    private fun HomeFragmentBinding.adaptSystemWindowInset() {
        Insetter.builder()
            .setOnApplyInsetsListener { view, insets, initialState ->
                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = initialState.margins.top + insets.getInsets(systemBars()).top
                }
            }
            .applyToView(headerHint.root)
        Insetter.builder()
            .setOnApplyInsetsListener { view, insets, initialState ->
                view.updatePadding(
                    top = initialState.paddings.top + insets.getInsets(systemBars()).top
                )
            }
            .applyToView(header.collapsingToolbar)
        Insetter.builder()
            .setOnApplyInsetsListener { view, insets, initialState ->
                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = initialState.margins.top + insets.getInsets(systemBars()).top
                }
            }
            .applyToView(filterContainerView)
        Insetter.builder()
            .setOnApplyInsetsListener { view, insets, initialState ->
                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin =
                        initialState.margins.bottom + insets.getInsets(systemBars()).bottom
                }
            }
            .applyToView(filterButton)
    }

    private fun HomeFragmentBinding.initViewState(viewModel: HomeViewModel) {
//        prepareSharedElements()
        pageAdapter = HomePageAdapter(this@HomeFragment)
        viewPager.adapter = pageAdapter
        header.apply {
            toolbar.setNavigationOnClickListener {
                systemViewModel.openNavigationMenu()
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
        header.tabs.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            header.tabDivider.isInvisible = scrollX == 0
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

    /** UI Renderer */

    private fun HomeHeaderHintBinding.render(uiModel: HomeHeaderUiModel) {
        hintLabel.setText(
            when (uiModel) {
                HomeHeaderUiModel.Now -> R.string.menu_now
                HomeHeaderUiModel.Plan -> R.string.menu_plan
                HomeHeaderUiModel.Favorite -> R.string.menu_favorite
            }
        )
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
}
