@file:Suppress("NOTHING_TO_INLINE")

package soup.movie.util

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

inline fun View.animateVisible(
    isVisible: Boolean,
    startDelay: Long = 0,
    duration: Long = 30
) {
    animate()
        .setStartDelay(startDelay)
        .setDuration(duration)
        .alpha(if (isVisible) 1f else 0f)
        .withEndAction { this.isVisible = isVisible }
}

inline fun View.animateInvisible(
    isInvisible: Boolean,
    startDelay: Long = 0,
    duration: Long = 30
) {
    animate()
        .setStartDelay(startDelay)
        .setDuration(duration)
        .alpha(if (isInvisible) 0f else 1f)
        .withEndAction { this.isInvisible = isInvisible }
}

inline fun View.animateGone(
    isGone: Boolean,
    startDelay: Long = 0,
    duration: Long = 300
) {
    animate()
        .setStartDelay(startDelay)
        .setDuration(duration)
        .alpha(if (isGone) 0f else 1f)
        .withEndAction { this.isGone = isGone }
}
