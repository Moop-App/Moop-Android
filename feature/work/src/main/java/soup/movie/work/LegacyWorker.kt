package soup.movie.work

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.DayOfWeek
import org.threeten.bp.temporal.ChronoUnit
import soup.movie.ext.isBest
import soup.movie.model.Movie
import soup.movie.model.repository.MoopRepository
import soup.movie.notification.NotificationBuilder
import soup.movie.util.currentTime
import soup.movie.util.plusDaysTo
import timber.log.Timber
import java.util.concurrent.TimeUnit

class LegacyWorker @WorkerInject constructor(
    @Assisted @ApplicationContext context: Context,
    @Assisted params: WorkerParameters,
    private val repository: MoopRepository,
    private val notificationBuilder: NotificationBuilder
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Timber.d("doWork: start!")
        return try {
            val movieList = getRecommendedMovieList()
            if (movieList.isNotEmpty()) {
                notificationBuilder.showLegacyNotification(movieList)
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

    private suspend fun getRecommendedMovieList(): List<Movie> {
        return withContext(Dispatchers.IO) {
            repository.updateAndGetNowMovieList().asSequence()
                .filter {
                    it.theater.run {
                        cgv != null && lotte != null && megabox != null
                    }
                }
                .filterIndexed { index, movie -> index < 3 || movie.isBest() }
                .take(6)
                .toList()
        }
    }

    companion object {

        private const val DEBUG = false
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
            if (DEBUG) {
                return 0
            }
            val current = currentTime()
            val rebirth = current
                .withHour(14)
                .plusDaysTo(DayOfWeek.FRIDAY)
                .plusWeeks(3)
            return current.until(rebirth, ChronoUnit.MINUTES)
        }
    }
}
