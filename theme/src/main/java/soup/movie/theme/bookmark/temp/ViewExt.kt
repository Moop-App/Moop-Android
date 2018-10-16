package soup.movie.theme.bookmark.temp

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter

//TODO: move to 'core' module

/** View */

inline fun View.setVisibleIf(predicate: () -> Boolean) {
    visibility = if (predicate()) View.VISIBLE else View.GONE
}

@BindingAdapter("android:visibleIf")
fun View.setVisibleIfInXml(predicate: Boolean) {
    visibility = if (predicate) View.VISIBLE else View.GONE
}

@BindingAdapter("android:backgroundTint")
fun View.setBackgroundTint(@ColorInt color: Int) {
    backgroundTintList = ColorStateList.valueOf(color)
}

/** ViewGroup */

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

/** ImageView */

@BindingAdapter("android:tint")
fun ImageView.setImageTint(@ColorInt color: Int) {
    imageTintList = ColorStateList.valueOf(color)
}
