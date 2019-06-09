package soup.movie.util

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

/**
 * The methods to get resources from current theme
 */

@ColorInt
fun Context.getColorAttr(@AttrRes attr: Int): Int {
    val ta = obtainStyledAttributes(intArrayOf(attr))
    @ColorInt val color = ta.getColor(0, 0)
    ta.recycle()
    return color
}
