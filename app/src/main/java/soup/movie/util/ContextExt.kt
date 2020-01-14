package soup.movie.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsIntent.COLOR_SCHEME_SYSTEM
import androidx.core.content.ContextCompat
import soup.movie.R
import soup.movie.data.model.Url

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

fun Context.executeWeb(url: Url?) {
    if (url == null) return
    CustomTabsIntent.Builder()
        .addDefaultShareMenuItem()
        .setColorScheme(COLOR_SCHEME_SYSTEM)
        .setShowTitle(true)
        .setStartAnimations(this, R.anim.fade_in, R.anim.fade_out)
        .setExitAnimations(this, R.anim.fade_in, R.anim.fade_out)
        .build()
        .launchUrl(this, Uri.parse(url))
}
