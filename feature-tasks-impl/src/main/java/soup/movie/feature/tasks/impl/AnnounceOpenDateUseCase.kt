package soup.movie.feature.tasks.impl

import soup.movie.data.repository.MovieRepository
import soup.movie.domain.movie.YYYY_MM_DD
import soup.movie.domain.movie.plusDaysTo
import soup.movie.domain.movie.today
import soup.movie.feature.tasks.NotificationBuilder
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
