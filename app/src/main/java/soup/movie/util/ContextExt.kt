package soup.movie.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/** YouTube */

fun Context.executeYoutube(id: String) {
    try {
        startActivity(createAppIntent(id))
    } catch (e: ActivityNotFoundException) {
        startActivity(createWebIntent(id))
    }
}

private fun createAppIntent(id: String): Intent {
    return Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
}

private fun createWebIntent(id: String): Intent {
    return Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$id"))
}

/** Cgv */

private const val PACKAGE_NAME_CGV = "com.cgv.android.movieapp"

fun Context.executeCgvApp() {
    val pkgName = PACKAGE_NAME_CGV
    if (executeApp(pkgName).not()) {
        executePlayStoreForApp(pkgName)
    }
}

private fun Context.executeApp(pkgName: String): Boolean {
    val launchIntent = packageManager.getLaunchIntentForPackage(pkgName)
    if (launchIntent != null) {
        startActivity(launchIntent)
        return true
    }
    return false
}

private fun Context.executePlayStoreForApp(pkgName: String) {
    try {
        startActivity(Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$pkgName")))
    } catch (e: ActivityNotFoundException) {
        startActivity(Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$pkgName")))
    }
}

private fun Context.executeWebPage(url: String) {
    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(webIntent)
}

/** Color */

fun Context.getColorCompat(@ColorRes colorResId: Int) =
        ContextCompat.getColor(this, colorResId)

fun Context.getColorStateListCompat(@ColorRes colorResId: Int) =
        ContextCompat.getColorStateList(this, colorResId)
