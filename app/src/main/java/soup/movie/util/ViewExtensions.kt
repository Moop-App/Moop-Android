package soup.movie.util

import android.content.res.ColorStateList
import android.util.Pair
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

/** View */

@BindingAdapter("android:visibleIf")
fun View.setVisibleIf(predicate: Boolean) {
    isVisible = predicate
}

@BindingAdapter("android:invisibleIf")
fun View.setInvisibleIf(predicate: Boolean) {
    isInvisible = predicate
}

@BindingAdapter("android:goneIf")
fun View.setGoneIf(predicate: Boolean) {
    isGone = predicate
}

fun View.setBackgroundColorResource(@ColorRes resId: Int) {
    setBackgroundColor(context.getColorCompat(resId))
}

@BindingAdapter("android:selected")
fun setSelected(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
}

@BindingAdapter("android:backgroundResource")
fun View.setBackgroundResourceInXml(resId: Int) {
    setBackgroundResource(resId)
}

infix fun View.with(tag: String): Pair<View, String> {
    return Pair.create(this, tag)
}

infix fun View.with(@StringRes tagId: Int): Pair<View, String> {
    return Pair.create(this, context.getString(tagId))
}

@BindingAdapter("android:backgroundTint")
fun View.setBackgroundTint(@ColorInt color: Int) {
    backgroundTintList = ColorStateList.valueOf(color)
}
