/*
 * Copyright 2023 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
