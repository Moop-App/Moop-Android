/*
 * Copyright 2021 SOUP
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
package soup.movie.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.threeten.bp.DayOfWeek
import org.threeten.bp.temporal.ChronoUnit
import soup.movie.model.OpenDateAlarm
import soup.movie.model.repository.MovieRepository
import soup.movie.notification.NotificationBuilder
import soup.movie.util.YYYY_MM_DD
import soup.movie.util.currentTime
import soup.movie.util.plusDaysTo
import soup.movie.util.today
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.math.max

@HiltWorker
class OpenDateAlarmWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: MovieRepository,
    private val notificationBuilder: NotificationBuilder
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Timber.d("doWork: start!")
        return try {
            if (repository.hasOpenDateAlarms()) {
                val alarms = getOpeningDateAlarmList()
                if (alarms.isNotEmpty()) {
                    notificationBuilder.showAlarmNotification(alarms)
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

    companion object {

        private const val DEBUG = false
        private const val TAG = "open_date_alarm"

        fun enqueuePeriodicWork(context: Context) {
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, createRequest())
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
}
