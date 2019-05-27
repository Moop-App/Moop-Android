package soup.movie.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import soup.movie.R

/**
 * The methods to get resources from current theme
 */

@ColorInt
fun Context.getColorPrimary(): Int {
    return getColorAttr(android.R.attr.colorPrimary)
}

@ColorInt
fun Context.getColorPrimaryDark(): Int {
    return getColorAttr(android.R.attr.colorPrimaryDark)
}

@ColorInt
fun Context.getColorAccent(): Int {
    return getColorAttr(android.R.attr.colorAccent)
}

@RequiresApi(Build.VERSION_CODES.O)
@ColorInt
fun Context.getColorError(): Int {
    return getColorAttr(android.R.attr.colorError)
}

@ColorInt
fun Context.getColorAttr(@AttrRes attr: Int): Int {
    val ta = obtainStyledAttributes(intArrayOf(attr))
    @ColorInt val color = ta.getColor(0, 0)
    ta.recycle()
    return color
}

fun Context.getDrawableAttr(@AttrRes attr: Int): Drawable? {
    val ta = obtainStyledAttributes(intArrayOf(attr))
    val drawable = ta.getDrawable(0)
    ta.recycle()
    return drawable
}

/**
 * The methods to get resources as theme
 */

@ColorInt
fun Context.getColorPrimary(@StyleRes theme: Int): Int {
    return getColorAttr(theme, R.attr.colorPrimary)
}

@ColorInt
fun Context.getColorPrimaryDark(@StyleRes theme: Int): Int {
    return getColorAttr(theme, R.attr.colorPrimaryDark)
}

@ColorInt
fun Context.getColorAccent(@StyleRes theme: Int): Int {
    return getColorAttr(theme, R.attr.colorAccent)
}

@RequiresApi(Build.VERSION_CODES.O)
@ColorInt
fun Context.getColorError(@StyleRes theme: Int): Int {
    return getColorAttr(theme, android.R.attr.colorError)
}

@ColorInt
fun Context.getColorAttr(@StyleRes theme: Int, @AttrRes attr: Int): Int {
    val ta = obtainStyledAttributes(theme, intArrayOf(attr))
    val color = ta.getColor(0, 0)
    ta.recycle()
    return color
}

fun Context.getDrawableAttr(@StyleRes theme: Int, @AttrRes attr: Int): Drawable? {
    val ta = obtainStyledAttributes(theme, intArrayOf(attr))
    val drawable = ta.getDrawable(0)
    ta.recycle()
    return drawable
}
