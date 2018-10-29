package soup.movie.data.helper

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.browser.customtabs.CustomTabsIntent
import soup.movie.BuildConfig
import soup.movie.R
import soup.movie.data.model.*
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

    protected abstract val packageName: String

    fun isInstalled(ctx: Context): Boolean {
        return ctx.isInstalledApp(packageName)
    }

    fun executePlayStore(ctx: Context) {
        ctx.executePlayStoreForApp(packageName)
    }

    fun executeApp(ctx: Context) {
        ctx.executeMarketApp(packageName)
    }
}

object Moop : MarketApp() {

    override val packageName = BuildConfig.APPLICATION_ID
}

object Cgv : MarketApp() {

    override val packageName = "com.cgv.android.movieapp"
    private val scheduleClass = "$packageName.reservation.movieschedule.MovieScheduleActivity"

    fun executeAppForSchedule(ctx: Context) {
        ctx.executeMarketApp(packageName, scheduleClass)
    }

    fun executeWeb(ctx: Context, movie: Movie) {
        movie.cgv?.let {
            ctx.executeWeb(detailWebUrl(it))
        }
    }

    fun executeMobileWeb(ctx: Context, movie: Movie) {
        movie.cgv?.let {
            ctx.executeWeb(detailMobileWebUrl(it))
        }
    }

    fun executeWebForSchedule(ctx: Context, movie: Movie) {
        movie.cgv?.let {
            ctx.executeWeb(reservationUrl(it))
        }
    }

    fun executeWeb(ctx: Context, theater: Theater) {
        ctx.executeWeb(detailWebUrl(theater))
    }

    private fun detailWebUrl(cgv: CgvInfo): String =
            "http://www.cgv.co.kr/movies/detail-view/?midx=${cgv.id}"

    private fun detailMobileWebUrl(cgv: CgvInfo): String =
            "http://m.cgv.co.kr/WebApp/MovieV4/movieDetail.aspx?MovieIdx=${cgv.id}"

    private fun reservationUrl(cgv: CgvInfo): String =
            "http://m.cgv.co.kr/quickReservation/Default.aspx?MovieIdx=${cgv.id}"

    private fun detailWebUrl(theater: Theater): String =
            "http://m.cgv.co.kr/WebApp/TheaterV4/TheaterDetail.aspx?tc=${theater.code}"
}

object LotteCinema : MarketApp() {

    override val packageName = "kr.co.lottecinema.lcm"

    fun executeAppForSchedule(ctx: Context) {
        executeApp(ctx)
    }

    fun executeWeb(ctx: Context, movie: Movie) {
        movie.lotte?.let {
            ctx.executeWeb(detailWebUrl(it))
        }
    }

    fun executeMobileWeb(ctx: Context, movie: Movie) {
        movie.lotte?.let {
            ctx.executeWeb(detailMobileWebUrl(it))
        }
    }

    fun executeWebForSchedule(ctx: Context, movie: Movie) {
        movie.lotte?.let {
            ctx.executeWeb(reservationUrl(it))
        }
    }

    fun executeWeb(ctx: Context, theater: Theater) {
        ctx.executeWeb(detailWebUrl(theater))
    }

    private fun detailWebUrl(lotte: LotteInfo): String =
            "http://www.lottecinema.co.kr/LCHS/Contents/Movie/Movie-Detail-View.aspx?movie=${lotte.id}"

    private fun detailMobileWebUrl(lotte: LotteInfo): String =
            "http://www.lottecinema.co.kr/LCMW/Contents/Movie/Movie-Detail-View.aspx?movie=${lotte.id}"

    private fun reservationUrl(lotte: LotteInfo): String =
            "http://www.lottecinema.co.kr/LCMW/Contents/Ticketing/ticketing.aspx"

    private fun detailWebUrl(theater: Theater): String =
            "http://www.lottecinema.co.kr/LCMW/Contents/Cinema/cinema-detail.aspx?cinemaID=${theater.code}"
}

object Megabox : MarketApp() {

    override val packageName = "com.megabox.mop"

    fun executeAppForSchedule(ctx: Context) {
        executeApp(ctx)
    }

    fun executeWeb(ctx: Context, movie: Movie) {
        movie.megabox?.let {
            ctx.executeWeb(detailWebUrl(it))
        }
    }

    fun executeMobileWeb(ctx: Context, movie: Movie) {
        movie.megabox?.let {
            ctx.executeWeb(detailMobileWebUrl(it))
        }
    }

    fun executeWebForSchedule(ctx: Context, movie: Movie) {
        movie.megabox?.let {
            ctx.executeWeb(reservationUrl(it))
        }
    }

    fun executeWeb(ctx: Context, theater: Theater) {
        ctx.executeWeb(detailWebUrl(theater))
    }

    private fun detailWebUrl(megabox: MegaboxInfo): String =
            "http://www.megabox.co.kr/?show=detail&rtnShowMovieCode=${megabox.id}"

    private fun detailMobileWebUrl(megabox: MegaboxInfo): String =
            "http://m.megabox.co.kr/?menuId=movie-detail&movieCode=${megabox.id}"

    private fun reservationUrl(megabox: MegaboxInfo): String =
            "http://m.megabox.co.kr/?menuId=booking&mBookingType=1"

    private fun detailWebUrl(theater: Theater): String =
            "http://m.megabox.co.kr/?menuId=theater-detail&cinema=${theater.code}"
}

object Naver : MarketApp() {

    override val packageName = "TODO"

    fun executeWeb(ctx: Context, movie: Movie) {
        movie.naver?.let {
            ctx.executeWeb(detailWebUrl(it))
        }
    }

    private fun detailWebUrl(naver: NaverInfo): String = naver.link
}

object Kakao : MarketApp() {

    override val packageName = "com.kakao.talk"
}

object YouTube : MarketApp() {

    override val packageName = "com.google.android.youtube"

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
            Uri.parse("vnd.youtube:$id")).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK or
                            Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT
                } else {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            }

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

    private fun createSearchAppIntent(query: String): Intent = Intent(
            Intent.ACTION_SEARCH)
            .setPackage(packageName)
            .putExtra("query", query).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK or
                            Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT
                } else {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            }

    private fun createSearchWebIntent(query: String): Intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.youtube.com/results?search_query=$query"))
}
