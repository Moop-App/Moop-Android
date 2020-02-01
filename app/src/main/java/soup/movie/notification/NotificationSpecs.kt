package soup.movie.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat.Builder
import soup.movie.R

object NotificationSpecs {

    const val TYPE_NOTICE = "Notice"
    const val TYPE_EVENT = "Event"

    fun initialize(application: Application) {
        NotificationChannels.initialize(application)
    }

    inline fun notifyAsNotice(ctx: Context, intercept: Builder.() -> Builder) {
        ctx.getNotificationManager()?.run {
            notify(1, Builder(ctx, NotificationChannels.NOTICE).intercept().build())
        }
    }

    inline fun notifyAsEvent(ctx: Context, intercept: Builder.() -> Builder) {
        ctx.getNotificationManager()?.run {
            notify(2, Builder(ctx, NotificationChannels.EVENT).intercept().build())
        }
    }

    inline fun notifyLegacy(ctx: Context, intercept: Builder.() -> Builder) {
        ctx.getNotificationManager()?.run {
            notify(3, Builder(ctx, NotificationChannels.EVENT).intercept().build())
        }
    }

    inline fun notifyOpenDateAlarm(ctx: Context, intercept: Builder.() -> Builder) {
        ctx.getNotificationManager()?.run {
            notify(4, Builder(ctx, NotificationChannels.OPEN_DATE_ALARM).intercept().build())
        }
    }

    fun createNotificationForTheaterMode(ctx: Context): Builder {
        return Builder(ctx, NotificationChannels.THEATER_MODE)
    }
}

fun Context.getNotificationManager(): NotificationManager? {
    return getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
}

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
