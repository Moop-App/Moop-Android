package soup.movie.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat.Builder
import soup.movie.R
import timber.log.Timber

object NotificationSpecs {

    fun initialize(application: Application) {
        NotificationChannels.initialize(application)
    }

    fun notifyAs(type: String,
                 ctx: Context,
                 intercept: (Builder) -> Builder) {
        when (type) {
            "Notice" -> notifyAsNotice(ctx, intercept)
            "Event" -> notifyAsEvent(ctx, intercept)
            else -> Timber.w("Try to notify as $type, but there is no consumer.")
        }
    }

    private inline fun notifyAsNotice(ctx: Context, intercept: Builder.() -> Builder) {
        ctx.getNotificationManager()?.run {
            notify(1, Builder(ctx, NotificationChannels.NOTICE).intercept().build())
        }
    }

    private inline fun notifyAsEvent(ctx: Context, intercept: Builder.() -> Builder) {
        ctx.getNotificationManager()?.run {
            notify(2, Builder(ctx, NotificationChannels.EVENT).intercept().build())
        }
    }

    private fun Context.getNotificationManager(): NotificationManager? {
        return getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    }

    private object NotificationChannels {

        const val NOTICE = "NTC"
        const val EVENT = "EVT" // Default

        fun initialize(application: Application) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                application.getNotificationManager()?.run {
                    val notice = NotificationChannel(
                            NOTICE,
                            application.getString(R.string.notification_channel_notice),
                            NotificationManager.IMPORTANCE_HIGH)
                    val event = NotificationChannel(
                            EVENT,
                            application.getString(R.string.notification_channel_event),
                            NotificationManager.IMPORTANCE_HIGH)
                    createNotificationChannels(listOf(notice, event))
                }
            }
        }
    }
}
