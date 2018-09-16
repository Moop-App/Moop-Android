package soup.movie.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.mapbox.mapboxsdk.annotations.Icon
import com.mapbox.mapboxsdk.annotations.IconFactory

fun Context.defaultIcon(): Icon = IconFactory.getInstance(this).defaultMarker()

fun Context.loadIcon(@DrawableRes id: Int): Icon? {
    return ResourcesCompat.getDrawable(resources, id, theme)?.run {
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
        IconFactory.getInstance(this@loadIcon).fromBitmap(bitmap)
    }
}

fun Context.loadIconOrDefault(@DrawableRes id: Int): Icon = loadIcon(id) ?: defaultIcon()
