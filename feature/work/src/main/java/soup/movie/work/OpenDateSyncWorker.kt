package soup.movie.work

import android.content.Context
import androidx.work.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import soup.movie.model.repository.MoopRepository
import soup.movie.work.di.ChildWorkerFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class OpenDateSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: MoopRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Timber.d("doWork: start!")
        return try {
            if (repository.hasOpenDateAlarms()) {
                withContext(Dispatchers.IO) {
                    repository.updatePlanMovieList()
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

    companion object {

        private const val TAG = "open_date_sync"

        fun enqueuePeriodicWork(context: Context) {
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, createRequest())
        }

        private fun createRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<OpenDateAlarmWorker>(14, TimeUnit.DAYS)
                .setInitialDelay(14, TimeUnit.DAYS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.MINUTES)
                .build()
        }
    }

    @AssistedInject.Factory
    interface Factory : ChildWorkerFactory
}
