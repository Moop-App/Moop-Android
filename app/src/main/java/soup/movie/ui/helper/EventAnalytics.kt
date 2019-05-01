package soup.movie.ui.helper

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.FirebaseAnalytics.Param
import soup.movie.data.model.Movie
import soup.movie.data.model.Trailer

class EventAnalytics(context: Context) {

    companion object {

        // General Type
        private const val CONTENT_TYPE_IMAGE = "Image"

        // Specific Type
        private const val CONTENT_TYPE_MOVIE = "Movie"
        private const val CONTENT_TYPE_TRAILER = "Trailer"

        private const val BRAND_CGV = "CGV"
        private const val BRAND_LOTTE = "Lotte"
        private const val BRAND_MEGABOX = "Megabox"

        private fun Movie.category(): String = when {
            isNow -> "Now"
            isPlan -> "Plan"
            else -> "Unknown"
        }
    }

    private val delegate by lazy {
        FirebaseAnalytics.getInstance(context)
    }

    private inline fun logEvent(var1: String, params: Bundle.() -> Unit) {
        delegate.logEvent(var1, Bundle().apply { params() })
    }

    private fun logButtonEvent(buttonName: String) {
        logEvent(Event.SELECT_CONTENT) {
            putString(Param.CONTENT_TYPE, "${buttonName}Button")
        }
    }

    private inline fun logButtonEvent(buttonName: String, moreParams: Bundle.() -> Unit) {
        logEvent(Event.SELECT_CONTENT) {
            putString(Param.CONTENT_TYPE, "${buttonName}Button")
            moreParams(this)
        }
    }

    /* Main */

    fun screen(activity: Activity, screenName: String) {
        delegate.setCurrentScreen(activity, screenName, null)
    }

    /* Main: Now, Plan */

    fun clickItem(index: Int, movie: Movie) {
        logEvent(Event.SELECT_CONTENT) {
            putInt(Param.INDEX, index)
            putString(Param.ITEM_ID, movie.id)
            putString(Param.ITEM_NAME, movie.title)
            putString(Param.ITEM_CATEGORY, movie.category())
            putString(Param.CONTENT_TYPE, CONTENT_TYPE_MOVIE)
        }
    }

    fun clickMenuFilter() {
        logButtonEvent("MenuFilter")
    }

    /* Main: Settings */

    fun clickMenuHelp() {
        logButtonEvent("MenuHelp")
    }

    /* Detail */

    fun clickPoster(movie: Movie) {
        logEvent(Event.SHARE) {
            putString(Param.ITEM_ID, movie.posterUrl)
            putString(Param.CONTENT_TYPE, CONTENT_TYPE_IMAGE)
        }
    }

    fun clickShare(movie: Movie) {
        logEvent(Event.SHARE) {
            putString(Param.ITEM_ID, movie.id)
            putString(Param.CONTENT_TYPE, CONTENT_TYPE_MOVIE)
        }
    }

    /* Detail: CGV */

    fun clickCgvInfo(movie: Movie) {
        logButtonEvent("Info") {
            putString(Param.ITEM_ID, movie.id)
            putString(Param.ITEM_NAME, movie.title)
            putString(Param.ITEM_CATEGORY, movie.category())
            putString(Param.ITEM_BRAND, BRAND_CGV)
        }
    }

    /* Detail: Lotte Cinema */

    fun clickLotteInfo(movie: Movie) {
        logButtonEvent("Info") {
            putString(Param.ITEM_ID, movie.id)
            putString(Param.ITEM_NAME, movie.title)
            putString(Param.ITEM_CATEGORY, movie.category())
            putString(Param.ITEM_BRAND, BRAND_LOTTE)
        }
    }

    /* Detail: Megabox */

    fun clickMegaboxInfo(movie: Movie) {
        logButtonEvent("Info") {
            putString(Param.ITEM_ID, movie.id)
            putString(Param.ITEM_NAME, movie.title)
            putString(Param.ITEM_CATEGORY, movie.category())
            putString(Param.ITEM_BRAND, BRAND_MEGABOX)
        }
    }

    /* Detail: Trailers */

    fun clickItem(trailer: Trailer) {
        logEvent(Event.SELECT_CONTENT) {
            putString(Param.ITEM_ID, trailer.youtubeId)
            putString(Param.ITEM_NAME, trailer.title)
            putString(Param.ITEM_BRAND, trailer.author)
            putString(Param.CONTENT_TYPE, CONTENT_TYPE_TRAILER)
        }
    }

    fun clickMoreTrailers(query: String) {
        logEvent(Event.SEARCH) {
            putString(Param.SEARCH_TERM, query)
        }
    }
}
