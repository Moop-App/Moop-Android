package soup.movie.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri

object MovieAppUtil {

    private const val PACKAGE_NAME_CGV = "com.cgv.android.movieapp"

    @JvmStatic
    fun executeCgvApp(ctx: Context) {
        val pkgName = PACKAGE_NAME_CGV
        if (!executeApp(ctx, pkgName)) {
            executePlayStoreForApp(ctx, pkgName)
        }
    }

    private fun executeApp(ctx: Context, pkgName: String): Boolean {
        val launchIntent = ctx.packageManager.getLaunchIntentForPackage(pkgName)
        if (launchIntent != null) {
            ctx.startActivity(launchIntent)
            return true
        }
        return false
    }

    private fun executePlayStoreForApp(ctx: Context, pkgName: String) {
        try {
            ctx.startActivity(Intent(
                    ACTION_VIEW,
                    Uri.parse("market://details?id=$pkgName")))
        } catch (e: ActivityNotFoundException) {
            ctx.startActivity(Intent(ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$pkgName")))
        }

    }

    private fun executeWebPage(ctx: Context, url: String) {
        val webIntent = Intent(ACTION_VIEW, Uri.parse(url))
        ctx.startActivity(webIntent)
    }
}
