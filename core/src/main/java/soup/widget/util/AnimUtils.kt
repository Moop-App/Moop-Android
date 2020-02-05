package soup.widget.util

import android.view.animation.Interpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

/**
 * Utility methods for working with animations.
 */
object AnimUtils {

    @JvmStatic
    val fastOutSlowInInterpolator: Interpolator by lazy { FastOutSlowInInterpolator() }
}
