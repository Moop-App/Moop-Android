package soup.movie.util

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/** Color */

fun Context.getColorCompat(@ColorRes colorResId: Int) =
        ContextCompat.getColor(this, colorResId)

fun Context.getColorStateListCompat(@ColorRes colorResId: Int) =
        ContextCompat.getColorStateList(this, colorResId)

/** Toast */

fun Context.showToast(msg: CharSequence) =
        Toast.makeText(this, msg, LENGTH_SHORT).show()
