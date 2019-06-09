package soup.movie.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import soup.movie.R
import soup.movie.notification.NotificationSpecs.TYPE_EVENT
import soup.movie.notification.NotificationSpecs.TYPE_NOTICE
import soup.movie.ui.main.MainActivity
import soup.movie.util.getColorCompat
import timber.log.Timber

class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(s: String?) {
        Timber.d("onNewToken: token=$s")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Timber.d("onMessageReceived: from=${remoteMessage?.from}")
        val data = remoteMessage?.data
        if (data != null) {
            Timber.d("Message data payload: $data")
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
    }

    private fun Context.notifyNotice(title: String, text: String) {
        NotificationSpecs.notifyAsNotice(this) {
            setSmallIcon(R.drawable.ic_notify_default)
            setContentTitle(title)
            setContentText(text)
            setAutoCancel(true)
            setContentIntent(getMainScreenIntent())
            setColor(getColorCompat(R.color.colorSecondary))
        }
    }

    private fun Context.notifyEvent(title: String, text: String) {
        NotificationSpecs.notifyAsEvent(this) {
            setSmallIcon(R.drawable.ic_notify_default)
            setContentTitle(title)
            setContentText(text)
            setAutoCancel(true)
            setContentIntent(getMainScreenIntent())
            setColor(getColorCompat(R.color.colorSecondary))
        }
    }

    private fun getMainScreenIntent(): PendingIntent {
        val intent = Intent(applicationContext, MainActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
    }
}
