@file:Suppress("NOTHING_TO_INLINE")

package soup.movie.util

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
