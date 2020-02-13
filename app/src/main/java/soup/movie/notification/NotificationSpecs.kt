package soup.movie.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat.Builder

object NotificationSpecs {

    const val TYPE_NOTICE = "Notice"
    const val TYPE_EVENT = "Event"

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

