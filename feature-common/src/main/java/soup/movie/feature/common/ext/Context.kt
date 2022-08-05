/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.feature.common.ext

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabsIntent
import soup.movie.feature.common.R

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
    return resolveActivity(ctx.packageManager) != null
}

fun Context.executeWeb(url: String?) {
    if (url == null) return
    startNonBrowserActivity(url, fallback = { startBrowserActivity(url) })
}

private fun Context.startBrowserActivity(url: String) {
    CustomTabsIntent.Builder()
        .setShareState(CustomTabsIntent.SHARE_STATE_ON)
        .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
        .setShowTitle(true)
        .setStartAnimations(this, R.anim.fade_in, R.anim.fade_out)
        .setExitAnimations(this, R.anim.fade_in, R.anim.fade_out)
        .build()
        .launchUrl(this, Uri.parse(url))
}

private fun Context.startNonBrowserActivity(url: String, fallback: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            fallback()
        }
    } else {
        fallback()
    }
}
