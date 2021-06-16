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
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import soup.movie.glide.GlideApp
import soup.movie.glide.GlideRequest
import java.io.File

/** ImageView */

fun ImageView.loadAsync(url: String?, @DrawableRes placeholder: Int? = null) {
    if (url == null) {
        GlideApp.with(context)
            .load(placeholder)
            .into(this)
    } else {
        loadAsync(
            url,
            block = {
                if (placeholder != null) {
                    placeholder(placeholder)
                }
                transition(withCrossFade())
            }
        )
    }
}

fun ImageView.loadAsync(url: String?, doOnEnd: () -> Unit) {
    loadAsync(
        url,
        block = {
            listener(createEndListener(doOnEnd))
        }
    )
}

private inline fun ImageView.loadAsync(url: String?, block: GlideRequest<Drawable>.() -> Unit) {
    GlideApp.with(context)
        .load(url)
        .apply(block)
        .priority(Priority.IMMEDIATE)
        .into(this)
}

private inline fun createEndListener(crossinline action: () -> Unit): RequestListener<Drawable> {
    return object : RequestListener<Drawable> {

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            action()
            return false
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            action()
            return false
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

fun Context.loadAsync(url: String): File {
    return GlideApp.with(this)
        .asFile()
        .load(url)
        .submit()
        .get()
}
