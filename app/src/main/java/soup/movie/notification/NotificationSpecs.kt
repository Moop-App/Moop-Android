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
package soup.movie.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat.Builder

object NotificationSpecs {

    const val TYPE_NOTICE = "Notice"
    const val TYPE_EVENT = "Event"

    inline fun notifyAsNotice(ctx: Context, intercept: Builder.() -> Builder) {
        ctx.getNotificationManager()?.run {
            notify(1, Builder(ctx, NotificationChannels.NOTICE).intercept().build())
        }
    }

    inline fun notifyAsEvent(ctx: Context, intercept: Builder.() -> Builder) {
        ctx.getNotificationManager()?.run {
            notify(2, Builder(ctx, NotificationChannels.EVENT).intercept().build())
        }
    }

    inline fun notifyLegacy(ctx: Context, intercept: Builder.() -> Builder) {
        ctx.getNotificationManager()?.run {
            notify(3, Builder(ctx, NotificationChannels.EVENT).intercept().build())
        }
    }

    inline fun notifyOpenDateAlarm(ctx: Context, intercept: Builder.() -> Builder) {
        ctx.getNotificationManager()?.run {
            notify(4, Builder(ctx, NotificationChannels.OPEN_DATE_ALARM).intercept().build())
        }
    }

    fun createNotificationForTheaterMode(ctx: Context): Builder {
        return Builder(ctx, NotificationChannels.THEATER_MODE)
    }
}

fun Context.getNotificationManager(): NotificationManager? {
    return getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
}
