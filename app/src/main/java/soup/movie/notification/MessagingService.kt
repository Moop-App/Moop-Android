package soup.movie.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import soup.movie.R
import soup.movie.data.helper.isInWeekOfCultureDay
import soup.movie.data.helper.today
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
                    TYPE_EVENT -> notifyEvent(originTitle = title, originText = text)
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
            setColor(getColorCompat(R.color.colorAccent))
        }
    }

    private fun Context.notifyEvent(originTitle: String, originText: String) {
        val title: String
        val text: String
        if (today().isInWeekOfCultureDay()) {
            //TODO: hard-coded description
            title = "문화의 날"
            text = "매달 마지막 수요일은 문화의 날입니다.\n이번주 상영하는 영화를 확인해보세요!"
        } else {
            title = originTitle
            text = originText
        }
        NotificationSpecs.notifyAsEvent(this) {
            setSmallIcon(R.drawable.ic_notify_default)
            setContentTitle(title)
            setContentText(text)
            setAutoCancel(true)
            setContentIntent(getMainScreenIntent())
            setColor(getColorCompat(R.color.colorAccent))
        }
    }

    private fun getMainScreenIntent(): PendingIntent {
        val intent = Intent(applicationContext, MainActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
    }
}
