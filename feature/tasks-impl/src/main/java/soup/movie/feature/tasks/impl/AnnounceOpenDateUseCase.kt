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

import soup.movie.data.repository.MovieRepository
import soup.movie.domain.movie.YYYY_MM_DD
import soup.movie.domain.movie.plusDaysTo
import soup.movie.domain.movie.today
import soup.movie.feature.notification.NotificationBuilder
import soup.movie.model.OpenDateAlarmModel
import java.time.DayOfWeek
import javax.inject.Inject

interface AnnounceOpenDateUseCase {
    suspend operator fun invoke()
}

class AnnounceOpenDateUseCaseImpl @Inject constructor(
    private val repository: MovieRepository,
    private val notificationBuilder: NotificationBuilder,
) : AnnounceOpenDateUseCase {
    override suspend fun invoke() {
        if (repository.hasOpenDateAlarms()) {
            val alarms = getOpeningDateAlarmList()
            if (alarms.isNotEmpty()) {
                notificationBuilder.showAlarmNotification(alarms)
            }
        }
    }

    private suspend fun getOpeningDateAlarmList(): List<OpenDateAlarmModel> {
        val nextMonday = today().plusDaysTo(DayOfWeek.MONDAY).YYYY_MM_DD()
        return repository.getOpenDateAlarmListUntil(nextMonday)
            .also { repository.deleteOpenDateAlarms(it) }
    }
}
