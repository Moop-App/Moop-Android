/*
 * Copyright 2018 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package soup.movie.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider
import io.reactivex.Observable
import soup.movie.BuildConfig
import soup.movie.data.model.Url
import soup.movie.util.glide.GlideApp
import java.io.File

/**
 * A class responsible for resolving an image as identified by Url into a sharable [Uri].
 */
class ImageUriProvider(context: Context) {

    // Only hold the app context to avoid leaks
    private val appContext = context.applicationContext

    operator fun invoke(url: Url): Observable<Uri> {
        return Observable.fromCallable {
            // Retrieve the image from Glide (hopefully cached) as a File
            val file = GlideApp.with(appContext)
                .asFile()
                .load(url)
                .submit()
                .get()

            // Glide cache uses an unfriendly & extension-less name. Massage it based on the original.
            val fileName = url.substring(url.lastIndexOf('/') + 1)
            File(file.parent, fileName)
                .apply { file.renameTo(this) }
                .toImageUri()
        }
    }

    operator fun invoke(file: File): Observable<Uri> {
        return Observable.fromCallable {
            file.toImageUri()
        }
    }

    operator fun invoke(bitmap: Bitmap): Observable<Uri> {
        return Observable.fromCallable {
            bitmap.toCacheFile(
                appContext,
                folderName = "image_manager_disk_cache",
                fileName = "share.jpg"
            ).toImageUri()
        }
    }

    @WorkerThread
    private fun File.toImageUri(): Uri {
        return FileProvider.getUriForFile(appContext, BuildConfig.FILES_AUTHORITY, this)
    }
}
