package soup.movie.data.helper

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import soup.movie.BuildConfig
import soup.movie.R
import soup.movie.data.model.Movie
import soup.movie.data.model.Theater
import soup.movie.data.model.Trailer
import soup.movie.util.getColorCompat
import soup.movie.util.startActivitySafely
import timber.log.Timber

private fun Context.isInstalledApp(pkgName: String): Boolean {
    return packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
            .find { it?.packageName == pkgName } != null
}

private fun Context.executeMarketApp(pkgName: String, className: String? = null) {
    if (executeApp(pkgName, className).not()) {
        executePlayStoreForApp(pkgName)
    }
}

private fun Context.executeApp(pkgName: String, className: String? = null): Boolean {
    if (className != null) {
        val launchIntent = Intent().apply {
            setClassName(pkgName, className)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            startActivity(launchIntent)
            return true
        } catch (e: ActivityNotFoundException) {
            Timber.w(e)
        }
    }
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

private fun Context.executeWeb(url: String) {
    CustomTabsIntent.Builder()
            .addDefaultShareMenuItem()
            .setToolbarColor(getColorCompat(R.color.white))
            .setSecondaryToolbarColor(getColorCompat(R.color.black))
            .setShowTitle(true)
            .setStartAnimations(this, R.anim.fade_in, R.anim.fade_out)
            .setExitAnimations(this, R.anim.fade_in, R.anim.fade_out)
            .build()
            .launchUrl(this, Uri.parse(url))

    //TODO: Prepare Fallback UI
    //val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    //startActivity(webIntent)
}

sealed class MarketApp {

    protected abstract val PACKAGE_NAME: String

    fun isInstalled(ctx: Context): Boolean {
        return ctx.isInstalledApp(PACKAGE_NAME)
    }

    fun executePlayStore(ctx: Context) {
        ctx.executePlayStoreForApp(PACKAGE_NAME)
    }

    fun executeApp(ctx: Context) {
        ctx.executeMarketApp(PACKAGE_NAME)
    }
}

object Moob : MarketApp() {

    override val PACKAGE_NAME = BuildConfig.APPLICATION_ID
}

object Cgv : MarketApp() {

    override val PACKAGE_NAME = "com.cgv.android.movieapp"
    private const val CLASS_SCHEDULE = "com.cjs.cgv.movieapp.reservation.movieschedule.MovieScheduleActivity"

    fun executeAppForSchedule(ctx: Context) {
        ctx.executeMarketApp(PACKAGE_NAME, CLASS_SCHEDULE)
    }

    fun executeWeb(ctx: Context, movie: Movie) {
        ctx.executeWeb(detailWebUrl(movie))
    }

    fun executeMobileWeb(ctx: Context, movie: Movie) {
        ctx.executeWeb(detailMobileWebUrl(movie))
    }

    fun executeWebForSchedule(ctx: Context, movie: Movie) {
        ctx.executeWeb(reservationUrl(movie))
    }

    fun executeWeb(ctx: Context, theater: Theater) {
        ctx.executeWeb(detailWebUrl(theater))
    }

    fun detailWebUrl(movie: Movie): String =
            "http://www.cgv.co.kr/movies/detail-view/?midx=${movie.id}"

    fun detailMobileWebUrl(movie: Movie): String =
            "http://m.cgv.co.kr/WebApp/MovieV4/movieDetail.aspx?MovieIdx=${movie.id}"

    private fun reservationUrl(movie: Movie): String =
            "http://m.cgv.co.kr/quickReservation/Default.aspx?MovieIdx=${movie.id}"

    private fun detailWebUrl(theater: Theater): String =
            "http://m.cgv.co.kr/WebApp/TheaterV4/TheaterDetail.aspx?tc=${theater.code}"
}

object LotteCinema : MarketApp() {

    override val PACKAGE_NAME = "TODO"

    private fun detailWebUrl(theater: Theater): String =
            "http://www.lottecinema.co.kr/LCMW/Contents/Cinema/cinema-detail.aspx?cinemaID=${theater.code}"

    fun executeWeb(ctx: Context, theater: Theater) {
        ctx.executeWeb(detailWebUrl(theater))
    }
}

object Megabox : MarketApp() {

    override val PACKAGE_NAME = "TODO"

    private fun detailWebUrl(theater: Theater): String =
            "http://m.megabox.co.kr/?menuId=theater-detail&cinema=${theater.code}"

    fun executeWeb(ctx: Context, theater: Theater) {
        ctx.executeWeb(detailWebUrl(theater))
    }
}

object Kakao : MarketApp() {

    override val PACKAGE_NAME = "com.kakao.talk"
}

object YouTube : MarketApp() {

    override val PACKAGE_NAME = "com.google.android.youtube"

    fun executeApp(ctx: Context, trailer: Trailer) {
        val id = trailer.youtubeId
        try {
            ctx.startActivity(createTrailerAppIntent(id))
        } catch (e: ActivityNotFoundException) {
            ctx.startActivitySafely(createTrailerWebIntent(id))
        }
    }

    private fun createTrailerAppIntent(id: String): Intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("vnd.youtube:$id"))

    private fun createTrailerWebIntent(id: String): Intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$id"))

    fun executeAppWithQuery(ctx: Context, movie: Movie) {
        val query = "${movie.title} 예고편"
        try {
            ctx.startActivity(createSearchAppIntent(query))
        } catch (e: ActivityNotFoundException) {
            ctx.startActivitySafely(createSearchWebIntent(query))
        }
    }

    private fun createSearchAppIntent(query: String): Intent =
            Intent(Intent.ACTION_SEARCH)
                    .setPackage(PACKAGE_NAME)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("query", query)

    private fun createSearchWebIntent(query: String): Intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.youtube.com/results?search_query=$query"))
}
