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
package soup.movie.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import soup.movie.util.Interpolators

/**
 * The following codes are copied from ContentLoadingProgressBar
 */
class ContentLoadingProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ProgressBar(context, attrs, defStyle) {

    private val moveDistance = 60 * context.resources.displayMetrics.density

    init {
        translationY = -moveDistance
    }

    var isInProgress: Boolean
        get() = visibility == View.VISIBLE
        set(value) {
            if (value) {
                animate().cancel()
                visibility = View.VISIBLE
                animate()
                    .setInterpolator(Interpolators.SPRING)
                    .setDuration(400)
                    .translationY(moveDistance)
                    .withEndAction(null)
            } else {
                animate().cancel()
                animate()
                    .setInterpolator(Interpolators.ACCELERATE_DECELERATE)
                    .setDuration(150)
                    .translationY(-moveDistance)
                    .withEndAction {
                        visibility = View.GONE
                    }
            }
        }
}
