/*
 * Copyright 2023 SOUP
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
package soup.movie.core.analytics.impl

import android.app.Activity
import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import soup.movie.core.analytics.EventAnalytics
import javax.inject.Inject
import javax.inject.Singleton

private typealias Params = Bundle.() -> Unit

@Singleton
class EventAnalyticsImpl @Inject constructor() : EventAnalytics {

    private val delegate by lazy {
        Firebase.analytics
    }

    private inline fun logEvent(name: String, params: Params) {
        delegate.logEvent(name, Bundle().apply { params() })
    }

    private inline fun logSelectEvent(params: Params) {
        delegate.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, Bundle().apply { params() })
    }

    private inline fun logShareEvent(params: Params) {
        delegate.logEvent(FirebaseAnalytics.Event.SHARE, Bundle().apply { params() })
    }

    // Common

    override fun screen(activity: Activity, screenName: String, screenClass: String?) {
        logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass ?: activity.javaClass.simpleName)
        }
    }

    // Main

    override fun clickMovie() {
        logSelectEvent {
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, CONTENT_TYPE_MOVIE)
        }
    }

    override fun clickMenuFilter() {
        logSelectEvent {
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, "MenuFilterButton")
        }
    }

    // Detail

    override fun clickPoster() {
        logShareEvent {
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, CONTENT_TYPE_POSTER)
        }
    }

    override fun clickShare() {
        logShareEvent {
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, CONTENT_TYPE_MOVIE)
        }
    }

    override fun clickCgvInfo() {
        logSelectEvent {
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, "InfoButton")
            putString(FirebaseAnalytics.Param.ITEM_BRAND, BRAND_CGV)
        }
    }

    override fun clickLotteInfo() {
        logSelectEvent {
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, "InfoButton")
            putString(FirebaseAnalytics.Param.ITEM_BRAND, BRAND_LOTTE)
        }
    }

    override fun clickMegaboxInfo() {
        logSelectEvent {
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, "InfoButton")
            putString(FirebaseAnalytics.Param.ITEM_BRAND, BRAND_MEGABOX)
        }
    }

    // Detail: Trailers

    override fun clickTrailer() {
        logSelectEvent {
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, CONTENT_TYPE_TRAILER)
            putString(FirebaseAnalytics.Param.ITEM_BRAND, BRAND_YOUTUBE)
        }
    }

    override fun clickMoreTrailers() {
        logEvent(FirebaseAnalytics.Event.SEARCH) {
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, CONTENT_TYPE_TRAILER)
            putString(FirebaseAnalytics.Param.ITEM_BRAND, BRAND_YOUTUBE)
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
