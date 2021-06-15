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

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import soup.movie.home.favorite.HomeFavoriteFragment
import soup.movie.home.now.HomeNowFragment
import soup.movie.home.plan.HomePlanFragment
import soup.movie.home.tab.HomeTabFragment

class HomePageAdapter(fragment: HomeFragment) : FragmentStateAdapter(fragment) {

    private val items = arrayOf<Fragment>(
        HomeNowFragment(),
        HomePlanFragment(),
        HomeFavoriteFragment()
    )

    override fun createFragment(position: Int): Fragment = items[position]

    override fun getItemCount(): Int = items.size

    fun getFragment(position: Int): HomeTabFragment? {
        return items.getOrNull(position) as? HomeTabFragment
    }

    fun scrollToTop(position: Int) {
        getFragment(position)?.scrollToTop()
    }
}
