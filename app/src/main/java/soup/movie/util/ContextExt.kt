package soup.movie.util

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/** Color */

fun Context.getColorCompat(@ColorRes colorResId: Int) =
        ContextCompat.getColor(this, colorResId)

fun Context.getColorStateListCompat(@ColorRes colorResId: Int) =
        ContextCompat.getColorStateList(this, colorResId)
