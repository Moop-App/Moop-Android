package soup.movie.util

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import soup.movie.R

fun Context.executeWeb(url: String?) {
    if (url == null) return
    CustomTabsIntent.Builder()
        .addDefaultShareMenuItem()
        .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
        .setShowTitle(true)
        .setStartAnimations(this, R.anim.fade_in, R.anim.fade_out)
        .setExitAnimations(this, R.anim.fade_in, R.anim.fade_out)
        .build()
        .launchUrl(this, Uri.parse(url))
}
