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
package soup.movie.util

import android.view.View
import androidx.core.view.postOnAnimationDelayed

private typealias OnClickListener = (View) -> Unit

fun View.setOnDebounceClickListener(delay: Long = 0, listener: OnClickListener?) {
    if (listener == null) {
        setOnClickListener(null)
    } else {
        setOnClickListener(
            OnDebounceClickListener {
                if (delay > 0) {
                    postOnAnimationDelayed(delay) {
                        run(listener)
                    }
                } else {
                    run(listener)
                }
            }
        )
    }
}

class OnDebounceClickListener(private val listener: OnClickListener) : View.OnClickListener {

    override fun onClick(v: View?) {
        val now = System.currentTimeMillis()
        if (now - lastTime < INTERVAL) return
        lastTime = now
        if (v != null) {
            listener(v)
        }
    }

    companion object {

        private const val INTERVAL: Long = 300

        private var lastTime: Long = 0
    }
}
