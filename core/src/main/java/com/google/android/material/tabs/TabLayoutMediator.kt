package com.google.android.material.tabs

import androidx.viewpager2.widget.ViewPager2

interface TitleProvider {

    fun getItemTitle(position: Int): CharSequence
}

fun TabLayout.setupWithViewPager2(
    viewPager2: ViewPager2,
    titleProvider: TitleProvider,
    autoRefresh: Boolean
) {
    TabLayoutMediator(this, viewPager2, autoRefresh) { tab, position ->
        tab.text = titleProvider.getItemTitle(position)
    }.attach()
}

fun TabLayout.setupWithViewPager2(
    viewPager2: ViewPager2,
    autoRefresh: Boolean,
    configuration: (tab: TabLayout.Tab, position: Int) -> Unit
) {
    TabLayoutMediator(this, viewPager2, autoRefresh, configuration).attach()
}
