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
package soup.movie.feature.notification.impl

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat.Builder
import soup.movie.feature.notification.R

object NotificationSpecs {

    fun notifyAsNotice(ctx: Context, intercept: Builder.() -> Builder) {
        initialize(ctx)
        ctx.getNotificationManager()?.run {
            notify(1, Builder(ctx, NotificationChannels.NOTICE).intercept().build())
        }
    }

    fun notifyAsEvent(ctx: Context, intercept: Builder.() -> Builder) {
        initialize(ctx)
        ctx.getNotificationManager()?.run {
            notify(2, Builder(ctx, NotificationChannels.EVENT).intercept().build())
        }
    }

    fun notifyLegacy(ctx: Context, intercept: Builder.() -> Builder) {
        initialize(ctx)
        ctx.getNotificationManager()?.run {
            notify(3, Builder(ctx, NotificationChannels.EVENT).intercept().build())
        }
    }

    fun notifyOpenDateAlarm(ctx: Context, intercept: Builder.() -> Builder) {
        initialize(ctx)
        ctx.getNotificationManager()?.run {
            notify(4, Builder(ctx, NotificationChannels.OPEN_DATE_ALARM).intercept().build())
        }
    }

    private fun initialize(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.getNotificationManager()?.run {
                val notice = NotificationChannel(
                    NotificationChannels.NOTICE,
                    context.getString(R.string.notification_channel_notice),
                    NotificationManager.IMPORTANCE_HIGH,
                )
                val event = NotificationChannel(
                    NotificationChannels.EVENT,
                    context.getString(R.string.notification_channel_event),
                    NotificationManager.IMPORTANCE_HIGH,
                )
                val openDateAlarm = NotificationChannel(
                    NotificationChannels.OPEN_DATE_ALARM,
                    context.getString(R.string.notification_channel_open_date_alarm),
                    NotificationManager.IMPORTANCE_HIGH,
                )
                createNotificationChannels(listOf(notice, event, openDateAlarm))
            }
        }
    }

    private fun Context.getNotificationManager(): NotificationManager? {
        return getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    }
}
