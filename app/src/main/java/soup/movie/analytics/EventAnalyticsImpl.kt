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
package soup.movie.analytics

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.FirebaseAnalytics.Param
import soup.movie.core.analytics.EventAnalytics

private typealias Params = Bundle.() -> Unit

class EventAnalyticsImpl(context: Context) : EventAnalytics {

    private val delegate by lazy {
        FirebaseAnalytics.getInstance(context)
    }

    private inline fun logEvent(name: String, params: Params) {
        delegate.logEvent(name, Bundle().apply { params() })
    }

    private inline fun logSelectEvent(params: Params) {
        delegate.logEvent(Event.SELECT_CONTENT, Bundle().apply { params() })
    }

    private inline fun logShareEvent(params: Params) {
        delegate.logEvent(Event.SHARE, Bundle().apply { params() })
    }

    /* Common */

    override fun screen(activity: Activity, screenName: String, screenClass: String?) {
        logEvent(Event.SCREEN_VIEW) {
            putString(Param.SCREEN_NAME, screenName)
            putString(Param.SCREEN_CLASS, screenClass ?: activity.javaClass.simpleName)
        }
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
