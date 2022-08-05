/*
 * Copyright 2018 SOUP
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
package soup.movie.device

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider
import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import soup.movie.BuildConfig
import soup.movie.feature.common.device.ImageUriProvider
import java.io.File
import java.io.FileOutputStream

/**
 * A class responsible for resolving an image as identified by Url into a sharable [Uri].
 */
class ImageUriProviderImpl(
    context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) : ImageUriProvider {

    // Only hold the app context to avoid leaks
    private val appContext = context.applicationContext

    override suspend operator fun invoke(url: String): Uri? {
        val request = ImageRequest.Builder(appContext)
            .data(url)
            .build()
        val result = appContext.imageLoader.execute(request).drawable
        val bitmap = (result as? BitmapDrawable)?.bitmap ?: return null
        return imageUriOf(
            bitmap.toCacheFile(
                appContext,
                folderName = CACHE_DIRECTORY_NAME,
                fileName = url.substring(url.lastIndexOf('/') + 1)
            )
        )
    }

    override suspend operator fun invoke(file: File): Uri {
        return imageUriOf(file)
    }

    override suspend operator fun invoke(bitmap: Bitmap): Uri {
        return imageUriOf(
            bitmap.toCacheFile(
                appContext,
                folderName = CACHE_DIRECTORY_NAME,
                fileName = "share.jpg"
            )
        )
    }

    @WorkerThread
    private suspend fun imageUriOf(file: File): Uri {
        return withContext(ioDispatcher) {
            FileProvider.getUriForFile(appContext, BuildConfig.FILES_AUTHORITY, file)
        }
    }

    private fun Bitmap.toCacheFile(
        context: Context,
        folderName: String? = null,
        fileName: String
    ): File {
        val cacheDir = if (folderName.isNullOrBlank()) {
            context.cacheDir
        } else {
            File(context.cacheDir, folderName).apply { mkdirs() }
        }
        return File(cacheDir, fileName).apply {
            FileOutputStream(this).use {
                compress(Bitmap.CompressFormat.JPEG, 100, it)
                it.flush()
            }
        }
    }

    companion object {

        /**
         * Same as [coil.util.Utils.CACHE_DIRECTORY_NAME].
         */
        private const val CACHE_DIRECTORY_NAME = "image_cache"
    }
}
