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
package soup.movie.push

import android.app.PendingIntent
import android.content.Context
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import soup.movie.feature.navigator.AppNavigator
import soup.movie.feature.notification.NotificationSpecs
import soup.movie.feature.notification.NotificationSpecs.TYPE_EVENT
import soup.movie.feature.notification.NotificationSpecs.TYPE_NOTICE
import soup.movie.log.Logger
import soup.movie.resources.R
import javax.inject.Inject

@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var navigator: AppNavigator

    override fun onNewToken(s: String) {
        Logger.d("onNewToken: token=$s")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Logger.d("onMessageReceived: from=${remoteMessage.from}")
        val data = remoteMessage.data
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

    private fun Context.notifyNotice(title: String, text: String) {
        NotificationSpecs.notifyAsNotice(this) {
            setSmallIcon(R.drawable.ic_notify_default)
            setContentTitle(title)
            setContentText(text)
            setAutoCancel(true)
            setContentIntent(createLauncherIntent())
        }
    }

    private fun Context.notifyEvent(title: String, text: String) {
        NotificationSpecs.notifyAsEvent(this) {
            setSmallIcon(R.drawable.ic_notify_default)
            setContentTitle(title)
            setContentText(text)
            setAutoCancel(true)
            setContentIntent(createLauncherIntent())
        }
    }

    private fun createLauncherIntent(): PendingIntent {
        val intent = navigator.createIntentToMain()
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
    }
}
