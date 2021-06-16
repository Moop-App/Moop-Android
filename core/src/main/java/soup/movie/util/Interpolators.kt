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
package soup.movie.util

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.PathInterpolator
import soup.movie.ext.lazyFast

object Interpolators {

    val FAST_OUT_SLOW_IN: Interpolator by lazyFast { PathInterpolator(0.4f, 0f, 0.2f, 1f) }
    val FAST_OUT_LINEAR_IN: Interpolator by lazyFast { PathInterpolator(0.4f, 0f, 1f, 1f) }
    val LINEAR_OUT_SLOW_IN: Interpolator by lazyFast { PathInterpolator(0f, 0f, 0.2f, 1f) }
    val ALPHA_IN: Interpolator by lazyFast { PathInterpolator(0.4f, 0f, 1f, 1f) }
    val ALPHA_OUT: Interpolator by lazyFast { PathInterpolator(0f, 0f, 0.8f, 1f) }
    val LINEAR: Interpolator by lazyFast { LinearInterpolator() }
    val ACCELERATE: Interpolator by lazyFast { AccelerateInterpolator() }
    val ACCELERATE_DECELERATE: Interpolator by lazyFast { AccelerateDecelerateInterpolator() }
    val DECELERATE_QUINT: Interpolator by lazyFast { DecelerateInterpolator(2.5f) }
    val PANEL_CLOSE_ACCELERATED: Interpolator by lazyFast { PathInterpolator(0.3f, 0f, 0.5f, 1f) }
    val SPRING by lazyFast { OvershootInterpolator(.7f) }
}
