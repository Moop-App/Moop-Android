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
