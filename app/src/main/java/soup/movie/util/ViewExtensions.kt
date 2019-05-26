package soup.movie.util

import android.util.Pair
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

/** View */

fun View.setBackgroundColorResource(@ColorRes resId: Int) {
    setBackgroundColor(context.getColorCompat(resId))
}

infix fun View.with(tag: String): Pair<View, String> {
    return Pair.create(this, tag)
}

infix fun View.with(@StringRes tagId: Int): Pair<View, String> {
    return Pair.create(this, context.getString(tagId))
}
