package soup.movie.ui.theater.mode.dnd

import android.app.NotificationManager
import android.app.NotificationManager.INTERRUPTION_FILTER_ALL
import android.app.NotificationManager.INTERRUPTION_FILTER_NONE
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.provider.Settings
import soup.movie.util.showToast

/**
 * Refer to {@link https://android--code.blogspot.com/2018/04/android-kotlin-turn-on-of-do-not.html}
 */

private typealias Listener = (Boolean) -> Unit

class DndModule(private val ctx: Context, private val listener: Listener? = null) {

    private val notificationManager by lazy {
        ctx.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    fun isEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when (notificationManager.currentInterruptionFilter) {
                INTERRUPTION_FILTER_NONE -> true
                else -> false
            }
        } else false
    }

    fun enable(): Boolean {
        return checkNotificationPolicyAccess {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                notificationManager.setInterruptionFilter(INTERRUPTION_FILTER_NONE)
                fireOnStateChanged()
            }
        }
    }

    fun disable(): Boolean {
        return checkNotificationPolicyAccess {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                notificationManager.setInterruptionFilter(INTERRUPTION_FILTER_ALL)
                fireOnStateChanged()
            }
        }
    }

    private inline fun checkNotificationPolicyAccess(executor: () -> Unit): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (notificationManager.isNotificationPolicyAccessGranted) {
                executor.invoke()
                return true
            } else {
                ctx.showToast("You need to grant notification policy access.")
                ctx.startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        } else {
            ctx.showToast("Device does not support this feature.")
        }
        return false
    }

    fun startTracking() {
        //TODO: register listener to system service
        fireOnStateChanged()
    }

    fun stopTracking() {
        //TODO: unregister listener to system service
    }

    private fun fireOnStateChanged() {
        listener?.invoke(isEnabled())
    }
}
