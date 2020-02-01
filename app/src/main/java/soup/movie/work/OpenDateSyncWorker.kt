package soup.movie.work

import android.content.Context
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import soup.movie.data.repository.MoopRepository
import soup.movie.di.ChildWorkerFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class OpenDateSyncWorker(
    appContext: Context,
    params: WorkerParameters,
    private val repository: MoopRepository
) : CoroutineWorker(appContext, params) {

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
                .enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.REPLACE, createRequest())
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

    class Factory @Inject constructor(
        private val repository: Provider<MoopRepository>
    ) : ChildWorkerFactory {

        override fun create(context: Context, params: WorkerParameters): ListenableWorker {
            return OpenDateSyncWorker(context, params, repository.get())
        }
    }
}
