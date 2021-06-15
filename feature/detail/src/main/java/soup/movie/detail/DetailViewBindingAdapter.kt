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
package soup.movie.detail

import android.view.View
import android.widget.ImageView

/**
 * TOMATOMETER: https://www.rottentomatoes.com/about
 */
fun ImageView.setTomatoMeterIcon(rottenTomatoes: String) {
    if (rottenTomatoes.contains('%')) {
        val score = rottenTomatoes.substring(0, rottenTomatoes.lastIndex).toIntOrNull() ?: 0
        if (score >= 60) {
            setImageResource(R.drawable.ic_rt_fresh)
        } else {
            setImageResource(R.drawable.ic_rt_rotten)
        }
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}
