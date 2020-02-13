@file:Suppress("NOTHING_TO_INLINE")

package soup.movie.ext

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("android:visibleIf")
fun setVisibleIf(view: View, predicate: Boolean) {
    view.isVisible = predicate
}

@BindingAdapter("android:invisibleIf")
fun setInvisibleIf(view: View, predicate: Boolean) {
    view.isInvisible = predicate
}

@BindingAdapter("android:goneIf")
fun setGoneIf(view: View, predicate: Boolean) {
    view.isGone = predicate
}

@BindingAdapter("android:selected")
fun setSelected(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
}

@BindingAdapter("android:backgroundResource")
fun setBackgroundResource(view: View, resId: Int) {
    view.setBackgroundResource(resId)
}

@BindingAdapter("android:backgroundTint")
fun setBackgroundTint(view: View, @ColorInt color: Int) {
    view.backgroundTintList = ColorStateList.valueOf(color)
}

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
