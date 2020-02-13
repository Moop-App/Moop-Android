package soup.movie.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import soup.movie.R
import soup.movie.ext.getColorCompat
import soup.movie.model.Movie
import soup.movie.model.OpenDateAlarm
import soup.movie.ui.main.MainActivity

class NotificationBuilderImpl(context: Context) :
    NotificationBuilder {

    private val applicationContext = context.applicationContext

    override fun showLegacyNotification(list: List<Movie>) = applicationContext.run {
        NotificationSpecs.notifyLegacy(this) {
            setStyle(NotificationCompat.BigTextStyle())
            setSmallIcon(R.drawable.ic_notify_default)
            setContentTitle(buildSpannedString { bold { append("간만에 영화 보는거 어때요? 👀🍿") } })
            setContentText(list.joinToString { it.title })
            setAutoCancel(true)
            setContentIntent(createLauncherIntent())
            setColor(getColorCompat(R.color.colorSecondary))
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
            setColor(getColorCompat(R.color.colorSecondary))
        }
    }

    private fun Context.createLauncherIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
    }
}
