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
