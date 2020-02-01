package soup.movie.work

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.work.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.temporal.ChronoUnit
import soup.movie.R
import soup.movie.data.repository.MoopRepository
import soup.movie.di.ChildWorkerFactory
import soup.movie.model.OpenDateAlarm
import soup.movie.notification.NotificationSpecs
import soup.movie.ui.main.MainActivity
import soup.movie.util.getColorCompat
import soup.movie.util.helper.YYYY_MM_DD
import soup.movie.util.helper.currentTime
import soup.movie.util.helper.plusDaysTo
import soup.movie.util.helper.today
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider
import kotlin.math.max

class OpenDateAlarmWorker(
    appContext: Context,
    params: WorkerParameters,
    private val repository: MoopRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        Timber.d("doWork: start!")
        return try {
            if (repository.hasOpenDateAlarms()) {
                val alarms = getOpeningDateAlarmList()
                if (alarms.isNotEmpty()) {
                    showAlarmNotification(alarms)
                }
            }
            Result.success()
        } catch (t: Throwable) {
            Timber.w(t)
            if (runAttemptCount <= 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    private suspend fun getOpeningDateAlarmList(): List<OpenDateAlarm> {
        val nextMonday = today().plusDaysTo(DayOfWeek.MONDAY).YYYY_MM_DD()
        return repository.getOpenDateAlarmListUntil(nextMonday)
            .also { repository.deleteOpenDateAlarms(it) }
    }

    private fun showAlarmNotification(list: List<OpenDateAlarm>) = applicationContext.run {
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

    companion object {

        private const val DEBUG = false
        private const val TAG = "open_date_alarm"

        fun enqueuePeriodicWork(context: Context) {
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.REPLACE, createRequest())
        }

        private fun createRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<OpenDateAlarmWorker>(2, TimeUnit.DAYS)
                .setInitialDelay(calculateInitialDelayMinutes(), TimeUnit.MINUTES)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
                .build()
        }

        private fun calculateInitialDelayMinutes(): Long {
            if (DEBUG) {
                return 0
            }
            val current = currentTime()
            val rebirth = if (current.hour < 13) {
                current.withHour(13)
            } else {
                current.withHour(13).plusDays(1)
            }
            return max(0, current.until(rebirth, ChronoUnit.MINUTES))
        }
    }

    class Factory @Inject constructor(
        private val repository: Provider<MoopRepository>
    ) : ChildWorkerFactory {

        override fun create(context: Context, params: WorkerParameters): ListenableWorker {
            return OpenDateAlarmWorker(context, params, repository.get())
        }
    }
}
