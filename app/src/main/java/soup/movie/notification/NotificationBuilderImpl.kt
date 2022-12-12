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

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import soup.movie.R
import soup.movie.feature.work.NotificationBuilder
import soup.movie.model.Movie
import soup.movie.model.OpenDateAlarm
import soup.movie.ui.main.MainActivity

class NotificationBuilderImpl(context: Context) : NotificationBuilder {

    private val applicationContext = context.applicationContext

    override fun showLegacyNotification(list: List<Movie>) = applicationContext.run {
        NotificationSpecs.notifyLegacy(this) {
            setStyle(NotificationCompat.BigTextStyle())
            setSmallIcon(R.drawable.ic_notify_default)
            setContentTitle(buildSpannedString { bold { append("간만에 영화 보는거 어때요? 👀🍿") } })
            setContentText(list.joinToString { it.title })
            setAutoCancel(true)
            setContentIntent(createLauncherIntent())
        }
    }

    override fun showAlarmNotification(list: List<OpenDateAlarm>) = applicationContext.run {
        NotificationSpecs.notifyOpenDateAlarm(this) {
            setStyle(NotificationCompat.BigTextStyle())
            setSmallIcon(R.drawable.ic_notify_default)
            setContentTitle(buildSpannedString { bold { append("관심가는 작품이 곧 개봉합니다! ⏰❤️") } })
            setContentText(list.joinToString { it.title })
            setAutoCancel(true)
            setContentIntent(createLauncherIntent())
        }
    }

    private fun Context.createLauncherIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_ONE_SHOT
        }
        return PendingIntent.getActivity(this, 0, intent, flags)
    }
}
