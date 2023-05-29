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

import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import dagger.hilt.android.qualifiers.ApplicationContext
import soup.movie.feature.navigator.AppNavigator
import soup.movie.feature.notification.NotificationBuilder
import soup.movie.model.MovieModel
import soup.movie.model.OpenDateAlarmModel
import soup.movie.resources.R
import javax.inject.Inject

class NotificationBuilderImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val navigator: AppNavigator,
) : NotificationBuilder {

    private val applicationContext = context.applicationContext

    override fun showLegacyNotification(list: List<MovieModel>) = applicationContext.run {
        soup.movie.feature.notification.NotificationSpecs.notifyLegacy(this) {
            setStyle(NotificationCompat.BigTextStyle())
            setSmallIcon(R.drawable.ic_notify_default)
            setContentTitle(buildSpannedString { bold { append("간만에 영화 보는거 어때요? 👀🍿") } })
            setContentText(list.joinToString { it.title })
            setAutoCancel(true)
            setContentIntent(createLauncherIntent())
        }
    }

    override fun showAlarmNotification(list: List<OpenDateAlarmModel>) = applicationContext.run {
        soup.movie.feature.notification.NotificationSpecs.notifyOpenDateAlarm(this) {
            setStyle(NotificationCompat.BigTextStyle())
            setSmallIcon(R.drawable.ic_notify_default)
            setContentTitle(buildSpannedString { bold { append("관심가는 작품이 곧 개봉합니다! ⏰❤️") } })
            setContentText(list.joinToString { it.title })
            setAutoCancel(true)
            setContentIntent(createLauncherIntent())
        }
    }

    private fun Context.createLauncherIntent(): PendingIntent {
        val intent = navigator.createIntentToMain()
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_ONE_SHOT
        }
        return PendingIntent.getActivity(this, 0, intent, flags)
    }
}
