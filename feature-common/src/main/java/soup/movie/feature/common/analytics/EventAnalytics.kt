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
package soup.movie.feature.common.analytics

import android.app.Activity

interface EventAnalytics {

    /* Common */
    fun screen(activity: Activity, screenName: String, screenClass: String?)

    /* Main */
    fun clickMovie()
    fun clickMenuFilter()

    /* Detail */
    fun clickPoster()
    fun clickShare()
    fun clickCgvInfo()
    fun clickLotteInfo()
    fun clickMegaboxInfo()

    /* Detail: Trailers */
    fun clickTrailer()
    fun clickMoreTrailers()
}
