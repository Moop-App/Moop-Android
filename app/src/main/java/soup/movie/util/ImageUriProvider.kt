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
import android.net.Uri
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import io.reactivex.Observable
import soup.movie.BuildConfig
import java.io.File

/**
 * A class responsible for resolving an image as identified by Url into a sharable [Uri].
 */
class ImageUriProvider(context: Context) {

    // Only hold the app context to avoid leaks
    private val appContext = context.applicationContext

    operator fun invoke(url: String): Observable<Uri> = Observable.fromCallable {
        // Retrieve the image from Glide (hopefully cached) as a File
        val file = Glide.with(appContext)
                .asFile()
                .load(url)
                .submit()
                .get()
        // Glide cache uses an unfriendly & extension-less name. Massage it based on the original.
        val fileName = url.substring(url.lastIndexOf('/') + 1)
        val renamed = File(file.parent, fileName)
        file.renameTo(renamed)
        return@fromCallable FileProvider.getUriForFile(appContext, BuildConfig.FILES_AUTHORITY, renamed)
    }
}
