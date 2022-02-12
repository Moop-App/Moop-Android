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
package soup.movie.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.imageLoader
import coil.load
import coil.request.ImageRequest

/** ImageView */

fun ImageView.loadAsync(url: String?, @DrawableRes placeholder: Int? = null) {
    if (url == null) {
        placeholder?.let { load(it) }
    } else {
        load(url) {
            if (placeholder != null) {
                placeholder(placeholder)
            }
            crossfade(true)
        }
    }
}

fun ImageView.setGrayscale(enable: Boolean) {
    colorFilter = if (enable) {
        ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
    } else {
        null
    }
}

suspend fun Context.loadAsync(url: String): Bitmap? {
    val request = ImageRequest.Builder(this)
        .data(url)
        .build()
    val result = imageLoader.execute(request).drawable
    return (result as? BitmapDrawable)?.bitmap
}
