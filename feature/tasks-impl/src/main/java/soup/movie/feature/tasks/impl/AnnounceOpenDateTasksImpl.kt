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
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import soup.movie.domain.movie.currentTime
import soup.movie.feature.tasks.AnnounceOpenDateTasks
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.max

class AnnounceOpenDateTasksImpl @Inject constructor(
    private val workManager: WorkManager,
) : AnnounceOpenDateTasks {

    override fun fetch() {
        val request = PeriodicWorkRequestBuilder<AnnounceOpenDateWorker>(
            2,
            TimeUnit.DAYS
        )
            .setInitialDelay(calculateInitialDelayMinutes(), TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
            .build()
        workManager.enqueueUniquePeriodicWork(
            AnnounceOpenDateWorker.TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun calculateInitialDelayMinutes(): Long {
        val current = currentTime()
        val rebirth = if (current.hour < 13) {
            current.withHour(13)
        } else {
            current.withHour(13).plusDays(1)
        }
        return max(0, current.until(rebirth, ChronoUnit.MINUTES))
    }
}
