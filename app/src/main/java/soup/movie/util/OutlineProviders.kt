package soup.movie.util

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import androidx.databinding.BindingAdapter

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

@BindingAdapter("clipToOval")
fun clipToOval(view: View, clip: Boolean) {
    view.clipToOutline = clip
    view.outlineProvider = if (clip) OvalOutlineProvider else null
}

@BindingAdapter("clipToRoundRect")
fun clipToRoundRect(view: View, clipRadius: Float) {
    view.clipToOutline = true
    view.outlineProvider = RoundRectOutlineProvider(clipRadius)
}
