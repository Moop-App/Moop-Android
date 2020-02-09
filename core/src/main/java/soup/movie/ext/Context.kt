package soup.movie.ext

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

/** Color */

fun Context.getColorCompat(@ColorRes colorResId: Int) =
        ContextCompat.getColor(this, colorResId)

fun Context.getColorStateListCompat(@ColorRes colorResId: Int) =
        ContextCompat.getColorStateList(this, colorResId)

/** Toast */

fun Context.showToast(msg: CharSequence) =
        Toast.makeText(this, msg, LENGTH_SHORT).show()

fun Context.showToast(@StringRes msgId: Int) =
        Toast.makeText(this, msgId, LENGTH_SHORT).show()

/** Activity */

fun Context.startActivitySafely(intent: Intent) {
    if (intent.isValid(this)) {
        startActivity(intent)
    } else {
        showToast("실행할 앱을 찾을 수 없습니다.")
    }
}

private fun Intent.isValid(ctx: Context): Boolean {
    val activities = ctx.packageManager?.queryIntentActivities(this, PackageManager.MATCH_DEFAULT_ONLY)
    return activities != null && activities.size > 0
}
