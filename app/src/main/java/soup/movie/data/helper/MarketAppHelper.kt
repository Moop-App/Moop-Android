package soup.movie.data.helper

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import soup.movie.R
import soup.movie.data.model.Movie
import android.content.pm.PackageManager

fun Context.isInstalledApp(pkgName: String): Boolean {
    return packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
            .find { it?.packageName == pkgName } != null
}

fun Context.executeMarketApp(pkgName: String) {
    if (executeApp(pkgName).not()) {
        executePlayStoreForApp(pkgName)
    }
}

internal fun Context.executeApp(pkgName: String): Boolean {
    val launchIntent = packageManager.getLaunchIntentForPackage(pkgName)
    if (launchIntent != null) {
        startActivity(launchIntent)
        return true
    }
    return false
}

internal fun Context.executePlayStoreForApp(pkgName: String) {
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

internal fun Context.executeWebPage(url: String) {
    CustomTabsIntent.Builder()
            .addDefaultShareMenuItem()
            .setToolbarColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setSecondaryToolbarColor(Color.BLACK)
            .setShowTitle(true)
            .setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
            .setExitAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
            .build()
            .launchUrl(this, Uri.parse(url))

    //TODO: Prepare Fallback UI
    //val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    //startActivity(webIntent)
}

object Cgv {

    const val PACKAGE_NAME = "com.cgv.android.movieapp"

    fun detailWebUrl(movie: Movie): String =
            "http://www.cgv.co.kr/movies/detail-view/?midx=${movie.id}"

    fun detailMobileWebUrl(movie: Movie): String =
            "http://m.cgv.co.kr/WebApp/MovieV4/movieDetail.aspx?MovieIdx=${movie.id}"

    fun reservationUrl(movie: Movie): String =
            "http://m.cgv.co.kr/quickReservation/Default.aspx?MovieIdx=${movie.id}"
}

object LotteCinema {

    const val PACKAGE_NAME = "TODO"
}

object Megabox {

    const val PACKAGE_NAME = "TODO"
}

object Kakao {

    const val PACKAGE_NAME = "com.kakao.talk"
}
