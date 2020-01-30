package soup.movie.work

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.DayOfWeek
import org.threeten.bp.temporal.ChronoUnit
import soup.movie.BuildConfig
import soup.movie.R
import soup.movie.data.repository.MoopRepository
import soup.movie.di.ChildWorkerFactory
import soup.movie.domain.model.isBest
import soup.movie.model.Movie
import soup.movie.notification.NotificationSpecs
import soup.movie.util.getColorCompat
import soup.movie.util.helper.currentTime
import soup.movie.util.helper.nextDayOfWeek
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class LegacyWorker(
    appContext: Context,
    params: WorkerParameters,
    private val repository: MoopRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val movieList = getRecommendedMovieList()
        return if (movieList.isNotEmpty()) {
            showLegacyNotification(movieList)
            Result.success()
        } else {
            if (runAttemptCount <= 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    private suspend fun getRecommendedMovieList(): List<Movie> {
        return withContext(Dispatchers.IO) {
            try {
                repository.updateAndGetNowMovieList().asSequence()
                    .filter {
                        it.theater.run {
                            cgv != null && lotte != null && megabox != null
                        }
                    }
                    .filterIndexed { index, movie -> index < 3 || movie.isBest() }
                    .take(6)
                    .toList()
            } catch (t: Throwable) {
                emptyList<Movie>()
            }
        }
    }

    private fun showLegacyNotification(list: List<Movie>) = applicationContext.run {
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

    private fun Context.createLauncherIntent(): PendingIntent {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            setPackage(BuildConfig.APPLICATION_ID)
        }
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
    }

    companion object {

        private const val TAG = "legacy"

        fun enqueueWork(context: Context) {
            WorkManager.getInstance(context)
                .enqueueUniqueWork(TAG, ExistingWorkPolicy.REPLACE, createRequest())
        }

        private fun createRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<LegacyWorker>()
                .setInitialDelay(calculateInitialDelayMinutes(), TimeUnit.MINUTES)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
                .build()
        }

        private fun calculateInitialDelayMinutes(): Long {
            if (BuildConfig.DEBUG) {
                return 1
            }
            val current = currentTime()
            val rebirth = current
                .withHour(14)
                .nextDayOfWeek(DayOfWeek.FRIDAY)
                .plusWeeks(3)
            return current.until(rebirth, ChronoUnit.MINUTES)
        }
    }

    class Factory @Inject constructor(
        private val repository: Provider<MoopRepository>
    ) : ChildWorkerFactory {

        override fun create(context: Context, params: WorkerParameters): ListenableWorker {
            return LegacyWorker(
                context,
                params,
                repository.get()
            )
        }
    }
}
