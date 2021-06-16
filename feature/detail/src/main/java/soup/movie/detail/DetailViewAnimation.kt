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
package soup.movie.detail

import android.animation.Animator
import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.animation.doOnEnd
import kotlin.math.hypot

interface DetailViewAnimation {

    fun View.showShareViewFrom(button: View) {
        visibility = View.VISIBLE
        startCircularRevealOf(button, 0f, diagonalLength()) {
            duration = 300
        }
    }

    fun View.hideShareViewTo(button: View) {
        startCircularRevealOf(button, diagonalLength(), 0f) {
            duration = 300
            doOnEnd {
                visibility = View.GONE
            }
        }
    }

    private inline fun View.startCircularRevealOf(
        target: View,
        startRadius: Float,
        endRadius: Float,
        block: Animator.() -> Unit
    ) {
        ViewAnimationUtils.createCircularReveal(
            this,
            target.centerX(),
            target.centerY(),
            startRadius,
            endRadius
        ).apply(block).start()
    }

    private fun View.centerX(): Int {
        return (right + left) / 2
    }

    private fun View.centerY(): Int {
        return (bottom + top) / 2
    }

    private fun View.diagonalLength(): Float {
        return hypot(width.toFloat(), height.toFloat())
    }
}
