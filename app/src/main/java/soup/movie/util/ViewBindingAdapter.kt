package soup.movie.util

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

/** View */

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
fun setBackgroundResourceInXml(view: View, resId: Int) {
    view.setBackgroundResource(resId)
}

@BindingAdapter("android:backgroundTint")
fun setBackgroundTint(view: View, @ColorInt color: Int) {
    view.backgroundTintList = ColorStateList.valueOf(color)
}
