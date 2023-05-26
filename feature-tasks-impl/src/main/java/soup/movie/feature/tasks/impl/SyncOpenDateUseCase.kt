package soup.movie.feature.tasks.impl

import soup.movie.data.repository.MovieRepository
import javax.inject.Inject

interface SyncOpenDateUseCase {
    suspend operator fun invoke()
}

class SyncOpenDateUseCaseImpl @Inject constructor(
    private val repository: MovieRepository,
) : SyncOpenDateUseCase {

    override suspend fun invoke() {
        if (repository.hasOpenDateAlarms()) {
            repository.updatePlanMovieList()
        }
    }
}
