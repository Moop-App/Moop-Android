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
package soup.movie.theater.edit

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TitleProvider

class TheaterEditPageAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle), TitleProvider {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CgvEditFragment()
            1 -> LotteEditFragment()
            else -> MegaboxEditFragment()
        }
    }

    override fun getItemCount(): Int = 3

    override fun getItemTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "CGV"
            1 -> "롯데시네마"
            else -> "메가박스"
        }
    }
}
