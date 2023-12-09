/*
 * Copyright 2017 SOUP
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
package soup.movie.feature.theatermap.internal

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableWrapper
import android.os.Build
import android.util.SparseArray
import androidx.annotation.RequiresApi
import soup.movie.log.Logger
import kotlin.math.max

/**
 * Utility class to handle icon treatments (e.g., shadow generation) for the Launcher icons.
 */
class LauncherIcons(context: Context) {

    private val shadowCache = SparseArray<Bitmap>()
    private val iconSize = context.resources.getDimensionPixelSize(android.R.dimen.app_icon_size)

    fun getAppIcon(context: Context, packageName: String): Drawable? {
        return try {
            val pm = context.packageManager
            val appInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.getApplicationInfo(packageName, PackageManager.ApplicationInfoFlags.of(0))
            } else {
                pm.getApplicationInfo(packageName, 0)
            }
            getShadowedIcon(pm.getApplicationIcon(appInfo))
        } catch (e: PackageManager.NameNotFoundException) {
            Logger.w(e)
            null
        }
    }

    fun getShadowedIcon(drawable: Drawable): Drawable {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return drawable
        }
        if (drawable !is AdaptiveIconDrawable) {
            return drawable
        }
        val shadow = getShadowBitmap(drawable)
        return ShadowDrawable(
            shadow,
            drawable,
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun getShadowBitmap(d: AdaptiveIconDrawable): Bitmap {
        val shadowSize = max(iconSize, d.intrinsicHeight)
        synchronized(shadowCache) {
            val shadow = shadowCache[shadowSize]
            if (shadow != null) {
                return shadow
            }
        }

        d.setBounds(0, 0, shadowSize, shadowSize)

        val blur = ICON_SIZE_BLUR_FACTOR * shadowSize
        val keyShadowDistance = ICON_SIZE_KEY_SHADOW_DELTA_FACTOR * shadowSize
        val bitmapSize = (shadowSize + 2 * blur + keyShadowDistance).toInt()
        val shadow = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(shadow)
        canvas.translate(blur + keyShadowDistance / 2, blur)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.TRANSPARENT

        // Draw ambient shadow
        paint.setShadowLayer(blur, 0f, 0f, AMBIENT_SHADOW_ALPHA shl 24)
        canvas.drawPath(d.iconMask, paint)

        // Draw key shadow
        canvas.translate(0f, keyShadowDistance)
        paint.setShadowLayer(blur, 0f, 0f, KEY_SHADOW_ALPHA shl 24)
        canvas.drawPath(d.iconMask, paint)

        canvas.setBitmap(null)
        synchronized(shadowCache) {
            shadowCache.put(shadowSize, shadow)
        }
        return shadow
    }

    /**
     * A drawable which draws a shadow bitmap behind a drawable
     */
    class ShadowDrawable : DrawableWrapper {

        private val state: MyConstantState

        constructor(shadow: Bitmap, dr: Drawable) : super(dr) {
            this.state =
                MyConstantState(
                    shadow,
                    dr.constantState!!,
                )
        }

        constructor(state: MyConstantState) : super(state.childState.newDrawable()) {
            this.state = state
        }

        override fun getConstantState(): ConstantState? {
            return state
        }

        override fun draw(canvas: Canvas) {
            val bounds = bounds
            canvas.drawBitmap(state.shadow, null, bounds, state.paint)
            canvas.save()
            // Ratio of child drawable size to shadow bitmap size
            val factor = 1 / (1 + 2 * ICON_SIZE_BLUR_FACTOR + ICON_SIZE_KEY_SHADOW_DELTA_FACTOR)

            canvas.translate(
                bounds.width() * factor * (ICON_SIZE_BLUR_FACTOR + ICON_SIZE_KEY_SHADOW_DELTA_FACTOR / 2),
                bounds.height() * factor * ICON_SIZE_BLUR_FACTOR,
            )
            canvas.scale(factor, factor)
            super.draw(canvas)
            canvas.restore()
        }

        class MyConstantState internal constructor(
            val shadow: Bitmap,
            val childState: ConstantState,
        ) : ConstantState() {

            val paint = Paint(Paint.FILTER_BITMAP_FLAG)

            override fun newDrawable(): Drawable {
                return ShadowDrawable(
                    this,
                )
            }

            override fun getChangingConfigurations(): Int {
                return childState.changingConfigurations
            }
        }
    }

    companion object {
        // Percent of actual icon size
        private const val ICON_SIZE_BLUR_FACTOR = 0.5f / 48

        // Percent of actual icon size
        private const val ICON_SIZE_KEY_SHADOW_DELTA_FACTOR = 1f / 48

        private const val KEY_SHADOW_ALPHA = 61
        private const val AMBIENT_SHADOW_ALPHA = 30
    }
}
