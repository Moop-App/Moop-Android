package soup.movie.feature.tasks.impl

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import soup.movie.feature.tasks.SyncOpenDateTasks
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SyncOpenDateTasksImpl @Inject constructor(
    private val workManager: WorkManager,
) : SyncOpenDateTasks {

    override fun fetch() {
        val request = PeriodicWorkRequestBuilder<SyncOpenDateWorker>(14, TimeUnit.DAYS)
            .setInitialDelay(14, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.MINUTES)
            .build()
        workManager.enqueueUniquePeriodicWork(
            SyncOpenDateWorker.TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
    }
}
