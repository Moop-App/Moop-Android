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
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import soup.movie.BuildConfig
import soup.movie.ext.loadAsync
import soup.movie.ext.toCacheFile
import java.io.File

/**
 * A class responsible for resolving an image as identified by Url into a sharable [Uri].
 */
class ImageUriProviderImpl(context: Context) :
    ImageUriProvider {

    // Only hold the app context to avoid leaks
    private val appContext = context.applicationContext

    override suspend operator fun invoke(url: String): Uri {
        // Retrieve the image from Glide (hopefully cached) as a File
        val file = appContext.loadAsync(url)

        // Glide cache uses an unfriendly & extension-less name. Massage it based on the original.
        val fileName = url.substring(url.lastIndexOf('/') + 1)
        return File(file.parent, fileName)
            .apply { file.renameTo(this) }
            .toImageUri()
    }

    override suspend operator fun invoke(file: File): Uri {
        return file.toImageUri()
    }

    override suspend operator fun invoke(bitmap: Bitmap): Uri {
        return bitmap.toCacheFile(
            appContext,
            folderName = "image_manager_disk_cache",
            fileName = "share.jpg"
        ).toImageUri()
    }

    @WorkerThread
    private suspend fun File.toImageUri(): Uri {
        return withContext(Dispatchers.IO) {
            FileProvider.getUriForFile(appContext, BuildConfig.FILES_AUTHORITY, this@toImageUri)
        }
    }
}
