package soup.movie.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import soup.movie.util.glide.GlideApp

/** View */

fun View.blockExtraTouchEvents() = setOnTouchListener { _, _ -> true }

@Suppress("UNCHECKED_CAST")
fun <T : View> inflate(context: Context, @LayoutRes resource: Int): T =
    View.inflate(context, resource, null) as T

@Suppress("UNCHECKED_CAST")
fun <T : View> inflate(context: Context, @LayoutRes resource: Int, root: ViewGroup): T =
    View.inflate(context, resource, root) as T

inline fun View.setVisibleIf(predicate: () -> Boolean) {
    visibility = if (predicate()) View.VISIBLE else View.GONE
}

inline fun View.setGoneIf(predicate: () -> Boolean) {
    visibility = if (predicate()) View.GONE else View.VISIBLE
}

@BindingAdapter("android:visibleIf")
fun View.setVisibleIfInXml(predicate: Boolean) {
    visibility = if (predicate) View.VISIBLE else View.GONE
}

@BindingAdapter("android:goneIf")
fun View.setGoneIfInXml(predicate: Boolean) {
    visibility = if (predicate) View.VISIBLE else View.GONE
}

fun View.setBackgroundColorResource(@ColorRes resId: Int) {
    setBackgroundColor(context.getColorCompat(resId))
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

/** ViewGroup */

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

/** ImageView */

@BindingAdapter("android:tint")
fun ImageView.setImageTint(@ColorInt color: Int) {
    imageTintList = ColorStateList.valueOf(color)
}

@BindingAdapter("android:srcUrl")
fun ImageView.loadAsync(url: String) {
    GlideApp.with(context)
        .load(url)
        .priority(Priority.IMMEDIATE)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun ImageView.loadAsync(url: String, requestListener: RequestListener<Drawable>) {
    GlideApp.with(context)
        .load(url)
        .listener(requestListener)
        .priority(Priority.IMMEDIATE)
        .into(this)
}

/* ContentLoadingProgressBar */

inline fun ContentLoadingProgressBar.showIf(predicate: () -> Boolean) {
    if (predicate()) show() else hide()
}
