package soup.movie.analytics

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.FirebaseAnalytics.Param
import soup.movie.ext.lazyFast

private typealias Params = Bundle.() -> Unit

class EventAnalyticsImpl(context: Context): EventAnalytics {

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

    override fun screen(activity: Activity, screenName: String) {
        delegate.setCurrentScreen(activity, screenName, null)
    }

    /* Main */

    override fun clickMovie() {
        logSelectEvent {
            putString(Param.CONTENT_TYPE, CONTENT_TYPE_MOVIE)
        }
    }

    override fun clickMenuFilter() {
        logSelectEvent {
            putString(Param.CONTENT_TYPE, "MenuFilterButton")
        }
    }

    /* Detail */

    override fun clickPoster() {
        logShareEvent {
            putString(Param.CONTENT_TYPE, CONTENT_TYPE_POSTER)
        }
    }

    override fun clickShare() {
        logShareEvent {
            putString(Param.CONTENT_TYPE, CONTENT_TYPE_MOVIE)
        }
    }

    override fun clickCgvInfo() {
        logSelectEvent {
            putString(Param.CONTENT_TYPE, "InfoButton")
            putString(Param.ITEM_BRAND, BRAND_CGV)
        }
    }

    override fun clickLotteInfo() {
        logSelectEvent {
            putString(Param.CONTENT_TYPE, "InfoButton")
            putString(Param.ITEM_BRAND, BRAND_LOTTE)
        }
    }

    override fun clickMegaboxInfo() {
        logSelectEvent {
            putString(Param.CONTENT_TYPE, "InfoButton")
            putString(Param.ITEM_BRAND, BRAND_MEGABOX)
        }
    }

    /* Detail: Trailers */

    override fun clickTrailer() {
        logSelectEvent {
            putString(Param.CONTENT_TYPE, CONTENT_TYPE_TRAILER)
            putString(Param.ITEM_BRAND, BRAND_YOUTUBE)
        }
    }

    override fun clickMoreTrailers() {
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
