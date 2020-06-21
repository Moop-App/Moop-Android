package soup.movie.util

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

object OvalOutlineProvider : ViewOutlineProvider() {

    override fun getOutline(view: View, outline: Outline) {
        outline.setOval(
            view.paddingLeft,
            view.paddingTop,
            view.width - view.paddingRight,
            view.height - view.paddingBottom
        )
    }
}

class RoundRectOutlineProvider(private val radius: Float) : ViewOutlineProvider() {

    override fun getOutline(view: View, outline: Outline) {
        outline.setRoundRect(
            view.paddingLeft,
            view.paddingTop,
            view.width - view.paddingRight,
            view.height - view.paddingBottom,
            radius
        )
    }
}

fun View.clipToOval(clip: Boolean) {
    clipToOutline = clip
    outlineProvider = if (clip) OvalOutlineProvider else null
}

fun View.clipToRoundRect(clipRadius: Float) {
    clipToOutline = true
    outlineProvider = RoundRectOutlineProvider(clipRadius)
}
