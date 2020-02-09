package soup.movie.util

import android.view.animation.*
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
