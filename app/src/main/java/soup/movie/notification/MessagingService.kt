package soup.movie.notification

import android.app.PendingIntent
import android.content.Intent
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import soup.movie.R
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
                NotificationSpecs.notifyAs(type, this) { it
                        .setSmallIcon(R.drawable.ic_notify_default)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setColor(getColorCompat(R.color.colorAccent))
                        .setAutoCancel(true)
                        .setContentIntent(getMainScreenIntent())
                }
            }
        }

        val notification = remoteMessage?.notification
        if (notification != null) {
            Timber.d("Message Notification: title=${notification.title}, body=${notification.body}")
        }
    }

    private fun getMainScreenIntent(): PendingIntent {
        val intent = Intent(applicationContext, MainActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
    }

    private fun printCurrentToken() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Get new Instance ID token
                        Timber.d("printCurrentToken: token=${task.result?.token}")
                    } else {
                        Timber.w(task.exception, "printCurrentToken: fail!!")
                    }
                }
    }
}
