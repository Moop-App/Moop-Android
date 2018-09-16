package soup.movie.util

import android.view.animation.*

object Interpolators {

    val FAST_OUT_SLOW_IN: Interpolator = PathInterpolator(0.4f, 0f, 0.2f, 1f)
    val FAST_OUT_LINEAR_IN: Interpolator = PathInterpolator(0.4f, 0f, 1f, 1f)
    val LINEAR_OUT_SLOW_IN: Interpolator = PathInterpolator(0f, 0f, 0.2f, 1f)
    val ALPHA_IN: Interpolator = PathInterpolator(0.4f, 0f, 1f, 1f)
    val ALPHA_OUT: Interpolator = PathInterpolator(0f, 0f, 0.8f, 1f)
    val LINEAR: Interpolator = LinearInterpolator()
    val ACCELERATE: Interpolator = AccelerateInterpolator()
    val ACCELERATE_DECELERATE: Interpolator = AccelerateDecelerateInterpolator()
    val DECELERATE_QUINT: Interpolator = DecelerateInterpolator(2.5f)
    val PANEL_CLOSE_ACCELERATED: Interpolator = PathInterpolator(0.3f, 0f, 0.5f, 1f)
}