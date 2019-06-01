package soup.movie.util

import android.util.Pair
import android.view.View
import androidx.annotation.ColorRes

/** View */

fun View.setBackgroundColorResource(@ColorRes resId: Int) {
    setBackgroundColor(context.getColorCompat(resId))
}

infix fun View.with(tag: String): Pair<View, String> {
    return Pair.create(this, tag)
}
