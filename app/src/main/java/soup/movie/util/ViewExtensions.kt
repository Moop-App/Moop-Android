package soup.movie.util

import android.util.Pair
import android.view.View

/** View */

infix fun View.with(tag: String): Pair<View, String> {
    return Pair.create(this, tag)
}
