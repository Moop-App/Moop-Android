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
@file:Suppress("NOTHING_TO_INLINE")

package soup.movie.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import android.view.View
import androidx.core.view.drawToBitmap
import java.io.File
import java.io.FileOutputStream

inline fun View.toCacheFile(folderName: String? = null, fileName: String): File {
    return drawToBitmap().toCacheFile(context, folderName = folderName, fileName = fileName)
}

inline fun Bitmap.toCacheFile(
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
            compress(JPEG, 100, it)
            it.flush()
        }
    }
}
