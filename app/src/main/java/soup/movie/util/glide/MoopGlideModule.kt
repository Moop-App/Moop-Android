package soup.movie.util.glide

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888
import com.bumptech.glide.load.DecodeFormat.PREFER_RGB_565
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class MoopGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDefaultRequestOptions(RequestOptions()
                // Prefer higher quality images unless we're on a low RAM device
                .format(if (context.isLowRamDevice()) PREFER_RGB_565 else PREFER_ARGB_8888)
                // Disable hardware bitmaps as they don't play nicely with Palette
                .disallowHardwareConfig())
        builder.setMemoryCache(LruResourceCache(
                MemorySizeCalculator.Builder(context)
                        .setMemoryCacheScreens(2f)
                        .build()
                        .memoryCacheSize.toLong()))
        builder.setBitmapPool(LruBitmapPool(
                MemorySizeCalculator.Builder(context)
                        .setBitmapPoolScreens(3f)
                        .build()
                        .bitmapPoolSize.toLong()))
        builder.setDiskCache(InternalCacheDiskCacheFactory(context))
    }

    private fun Context.isLowRamDevice(): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as? ActivityManager
        return activityManager?.isLowRamDevice ?: false
    }

    override fun isManifestParsingEnabled(): Boolean = false
}
