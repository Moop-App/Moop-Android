package soup.movie.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.postOnAnimationDelayed
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
