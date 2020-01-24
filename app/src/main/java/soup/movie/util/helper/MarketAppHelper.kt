package soup.movie.util.helper

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import soup.movie.model.Theater
import soup.movie.model.Trailer
import soup.movie.ui.map.TheaterMarkerUiModel
import soup.movie.util.executeWeb
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

    override val packageName = "soup.movie"
}

object Cgv : MarketApp() {

    override val packageName = "com.cgv.android.movieapp"

    fun executeMobileWeb(ctx: Context, movieId: String) {
        ctx.executeWeb(detailMobileWebUrl(movieId))
    }

    fun executeWeb(ctx: Context, theater: Theater) {
        ctx.executeWeb(detailWebUrl(theater.code))
    }

    fun executeWeb(ctx: Context, theater: TheaterMarkerUiModel) {
        ctx.executeWeb(detailWebUrl(theater.code))
    }

    private fun detailMobileWebUrl(movieId: String): String =
            "https://m.cgv.co.kr/WebApp/MovieV4/movieDetail.aspx?MovieIdx=$movieId"

    private fun detailWebUrl(theaterCode: String): String =
            "https://m.cgv.co.kr/WebApp/TheaterV4/TheaterDetail.aspx?tc=$theaterCode"
}

object LotteCinema : MarketApp() {

    override val packageName = "kr.co.lottecinema.lcm"

    fun executeMobileWeb(ctx: Context, movieId: String) {
        ctx.executeWeb(detailMobileWebUrl(movieId))
    }

    fun executeWeb(ctx: Context, theater: Theater) {
        ctx.executeWeb(detailWebUrl(theater.code))
    }

    fun executeWeb(ctx: Context, theater: TheaterMarkerUiModel) {
        ctx.executeWeb(detailWebUrl(theater.code))
    }

    private fun detailMobileWebUrl(movieId: String): String =
            "https://www.lottecinema.co.kr/NLCMW/movie/moviedetailview?movie=$movieId"

    private fun detailWebUrl(theaterCode: String): String =
            "https://www.lottecinema.co.kr/NLCMW/Cinema/Detail?cinemaID=$theaterCode"
}

object Megabox : MarketApp() {

    override val packageName = "com.megabox.mop"

    fun executeMobileWeb(ctx: Context, movieId: String) {
        ctx.executeWeb(detailMobileWebUrl(movieId))
    }

    fun executeWeb(ctx: Context, theater: Theater) {
        ctx.executeWeb(detailWebUrl(theater.code))
    }

    fun executeWeb(ctx: Context, theater: TheaterMarkerUiModel) {
        ctx.executeWeb(detailWebUrl(theater.code))
    }

    private fun detailMobileWebUrl(movieId: String): String =
            "https://m.megabox.co.kr/?menuId=movie-detail&movieCode=$movieId"

    private fun detailWebUrl(theaterCode: String): String =
            "https://m.megabox.co.kr/?menuId=theater-detail&cinema=$theaterCode"
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
            Uri.parse("https://www.youtube.com/watch?v=$id"))

    fun executeAppWithQuery(ctx: Context, movieTitle: String) {
        val query = "$movieTitle 예고편"
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
