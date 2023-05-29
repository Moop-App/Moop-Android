/*
 * Copyright 2023 SOUP
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
import dagger.hilt.android.qualifiers.ApplicationContext
import soup.movie.feature.navigator.AppNavigator
import soup.movie.feature.notification.ShowPushNotificationUseCase
import soup.movie.log.Logger
import soup.movie.resources.R
import javax.inject.Inject

class ShowPushNotificationUseCaseImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val navigator: AppNavigator,
) : ShowPushNotificationUseCase {

    override operator fun invoke(data: Map<String, String?>) {
        Logger.d("Message data payload: $data")
        val type: String? = data["type"]
        val title: String? = data["title"]
        val text: String? = data["text"]
        if (type != null && title != null && text != null) {
            when (type) {
                TYPE_NOTICE -> notifyNotice(title = title, text = text)
                TYPE_EVENT -> notifyEvent(title = title, text = text)
            }
        }
    }

    private fun notifyNotice(title: String, text: String) {
        NotificationSpecs.notifyAsNotice(context) {
            setSmallIcon(R.drawable.ic_notify_default)
            setContentTitle(title)
            setContentText(text)
            setAutoCancel(true)
            setContentIntent(createLauncherIntent())
        }
    }

    private fun notifyEvent(title: String, text: String) {
        NotificationSpecs.notifyAsEvent(context) {
            setSmallIcon(R.drawable.ic_notify_default)
            setContentTitle(title)
            setContentText(text)
            setAutoCancel(true)
            setContentIntent(createLauncherIntent())
        }
    }

    private fun createLauncherIntent(): PendingIntent {
        val intent = navigator.createIntentToMain()
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
    }

    companion object {
        private const val TYPE_NOTICE = "Notice"
        private const val TYPE_EVENT = "Event"
    }
}
