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
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import soup.movie.domain.movie.currentTime
import soup.movie.domain.movie.plusDaysTo
import soup.movie.feature.tasks.RecommendMoviesTasks
import java.time.DayOfWeek
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RecommendMoviesTasksImpl @Inject constructor(
    private val workManager: WorkManager,
) : RecommendMoviesTasks {

    override fun fetch() {
        val request = OneTimeWorkRequestBuilder<RecommendMoviesWorker>()
            .setInitialDelay(calculateInitialDelayMinutes(), TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
            .build()
        workManager.enqueueUniqueWork(
            RecommendMoviesWorker.TAG,
            ExistingWorkPolicy.REPLACE,
            request,
        )
    }

    override fun cancel() {
        workManager.cancelUniqueWork(RecommendMoviesWorker.TAG)
    }

    private fun calculateInitialDelayMinutes(): Long {
        val current = currentTime()
        val rebirth = current
            .withHour(14)
            .plusDaysTo(DayOfWeek.FRIDAY)
            .plusWeeks(3)
        return current.until(rebirth, ChronoUnit.MINUTES)
    }
}
