package soup.movie.analytics

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.FirebaseAnalytics.Param
import soup.movie.util.lazyFast

private typealias Params = Bundle.() -> Unit

class EventAnalytics(context: Context) {

    private val delegate by lazyFast {
        FirebaseAnalytics.getInstance(context)
    }

    private inline fun logEvent(var1: String, params: Params) {
        delegate.logEvent(var1, Bundle().apply { params() })
    }

    private inline fun logSelectEvent(params: Params) {
        delegate.logEvent(Event.SELECT_CONTENT, Bundle().apply { params() })
    }

    private inline fun logShareEvent(params: Params) {
        delegate.logEvent(Event.SHARE, Bundle().apply { params() })
    }

    /* Common */

    fun screen(activity: Activity, screenName: String) {
        delegate.setCurrentScreen(activity, screenName, null)
    }

    /* Main */

    fun clickMovie() {
        logSelectEvent {
            putString(Param.CONTENT_TYPE, CONTENT_TYPE_MOVIE)
        }
    }

    fun clickMenuFilter() {
        logSelectEvent {
            putString(Param.CONTENT_TYPE, "MenuFilterButton")
        }
    }

    /* Detail */

    fun clickPoster() {
        logShareEvent {
            putString(Param.CONTENT_TYPE, CONTENT_TYPE_POSTER)
        }
    }

    fun clickShare() {
        logShareEvent {
            putString(Param.CONTENT_TYPE, CONTENT_TYPE_MOVIE)
        }
    }

    fun clickCgvInfo() {
        logSelectEvent {
            putString(Param.CONTENT_TYPE, "InfoButton")
            putString(Param.ITEM_BRAND, BRAND_CGV)
        }
    }

    fun clickLotteInfo() {
        logSelectEvent {
            putString(Param.CONTENT_TYPE, "InfoButton")
            putString(Param.ITEM_BRAND, BRAND_LOTTE)
        }
    }

    fun clickMegaboxInfo() {
        logSelectEvent {
            putString(Param.CONTENT_TYPE, "InfoButton")
            putString(Param.ITEM_BRAND, BRAND_MEGABOX)
        }
    }

    /* Detail: Trailers */

    fun clickTrailer() {
        logSelectEvent {
            putString(Param.CONTENT_TYPE, CONTENT_TYPE_TRAILER)
            putString(Param.ITEM_BRAND, BRAND_YOUTUBE)
        }
    }

    fun clickMoreTrailers() {
        logEvent(Event.SEARCH) {
            putString(Param.CONTENT_TYPE, CONTENT_TYPE_TRAILER)
            putString(Param.ITEM_BRAND, BRAND_YOUTUBE)
        }
    }

    companion object {

        private const val CONTENT_TYPE_POSTER = "Image"
        private const val CONTENT_TYPE_MOVIE = "Movie"
        private const val CONTENT_TYPE_TRAILER = "Trailer"

        private const val BRAND_CGV = "CGV"
        private const val BRAND_LOTTE = "Lotte"
        private const val BRAND_MEGABOX = "Megabox"
        private const val BRAND_YOUTUBE = "YouTube"
    }
}
