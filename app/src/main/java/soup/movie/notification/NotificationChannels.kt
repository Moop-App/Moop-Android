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

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import soup.movie.R

object NotificationChannels {

    const val NOTICE = "NTC"
    const val EVENT = "EVT" // Default
    const val THEATER_MODE = "THM"
    const val OPEN_DATE_ALARM = "ODA"

    internal fun initialize(application: Application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            application.getNotificationManager()?.run {
                val notice = NotificationChannel(
                    NOTICE,
                    application.getString(R.string.notification_channel_notice),
                    NotificationManager.IMPORTANCE_HIGH
                )
                val event = NotificationChannel(
                    EVENT,
                    application.getString(R.string.notification_channel_event),
                    NotificationManager.IMPORTANCE_HIGH
                )
                val theaterMode = NotificationChannel(
                    THEATER_MODE,
                    application.getString(R.string.notification_channel_theater_mode),
                    NotificationManager.IMPORTANCE_HIGH
                )
                val openDateAlarm = NotificationChannel(
                    OPEN_DATE_ALARM,
                    application.getString(R.string.notification_channel_open_date_alarm),
                    NotificationManager.IMPORTANCE_HIGH
                )
                createNotificationChannels(listOf(notice, event, theaterMode, openDateAlarm))
            }
        }
    }
}
