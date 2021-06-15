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
package soup.movie.home.tab

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.isScrolling
import androidx.recyclerview.widget.isTop
import soup.movie.ui.base.OnBackPressedListener

abstract class HomeTabFragment : Fragment, OnBackPressedListener {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    abstract fun scrollToTop()

    protected fun RecyclerView.scrollToTopInternal(force: Boolean = false): Boolean {
        if (isTop().not()) {
            if (isScrolling()) {
                stopScroll()
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
            return true
        }
        if (force) {
            stopScroll()
            scrollToPosition(0)
        }
        return false
    }
}
