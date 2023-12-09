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
package soup.movie.core.imageloading.impl

import android.content.Context
import androidx.startup.Initializer
import coil.Coil
import coil.ImageLoader

class CoilInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        val imageLoader = ImageLoader.Builder(context)
            .crossfade(true)
            .build()
        Coil.setImageLoader(imageLoader)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
