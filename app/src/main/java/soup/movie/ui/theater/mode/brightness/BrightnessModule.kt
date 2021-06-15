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
package soup.movie.ui.theater.mode.brightness

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.provider.Settings.System.SCREEN_BRIGHTNESS
import android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE
import android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
import android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
import android.provider.Settings.System.canWrite
import android.provider.Settings.System.getInt
import android.provider.Settings.System.putInt
import soup.movie.ext.showToast

/**
 * Refer to {@link https://android--code.blogspot.com/2018/04/android-kotlin-turn-on-of-do-not.html}
 */
class BrightnessModule(private val ctx: Context, private val listener: (Boolean) -> Unit) {

    private val contentResolver by lazy {
        ctx.contentResolver
    }

    fun isEnabled(): Boolean = isMinBrightness() && isManualMode()

    private fun getScreenBrightness(): Int =
        getInt(contentResolver, SCREEN_BRIGHTNESS, MAX_SCREEN_BRIGHTNESS)

    private fun isMinBrightness(): Boolean =
        getScreenBrightness() <= MIN_SCREEN_BRIGHTNESS

    private fun isMaxBrightness(): Boolean =
        getScreenBrightness() >= MAX_SCREEN_BRIGHTNESS

    private fun getScreenBrightnessMode(): Int =
        getInt(contentResolver, SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_MANUAL)

    private fun isManualMode(): Boolean =
        getScreenBrightnessMode() == SCREEN_BRIGHTNESS_MODE_MANUAL

    private fun isAutomaticMode(): Boolean =
        getScreenBrightnessMode() == SCREEN_BRIGHTNESS_MODE_AUTOMATIC

    fun enable(): Boolean {
        return checkManageWriteSettings {
            putInt(contentResolver, SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
            putInt(contentResolver, SCREEN_BRIGHTNESS, MIN_SCREEN_BRIGHTNESS)
            fireOnStateChanged()
        }
    }

    fun disable(): Boolean {
        return checkManageWriteSettings {
            putInt(contentResolver, SCREEN_BRIGHTNESS, MAX_SCREEN_BRIGHTNESS)
            putInt(contentResolver, SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_MANUAL)
            fireOnStateChanged()
        }
    }

    private inline fun checkManageWriteSettings(executor: () -> Unit): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (canWrite(ctx)) {
                executor.invoke()
                return true
            } else {
                ctx.showToast("You need to grant manage write settings.")
                ctx.startActivity(
                    Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        } else {
            ctx.showToast("Device does not support this feature.")
        }
        return false
    }

    fun startTracking() {
        // TODO: register listener to system service
        fireOnStateChanged()
    }

    fun stopTracking() {
        // TODO: unregister listener to system service
    }

    private fun fireOnStateChanged() {
        listener.invoke(isEnabled())
    }

    companion object {

        const val MIN_SCREEN_BRIGHTNESS = 0
        const val MAX_SCREEN_BRIGHTNESS = 255
    }
}
