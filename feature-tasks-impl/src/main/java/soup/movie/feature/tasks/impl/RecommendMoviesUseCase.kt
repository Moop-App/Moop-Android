package soup.movie.feature.tasks.impl

import soup.movie.data.repository.MovieRepository
import soup.movie.domain.movie.isBest
import soup.movie.feature.tasks.NotificationBuilder
import soup.movie.model.MovieModel
import javax.inject.Inject

interface RecommendMoviesUseCase {
    suspend operator fun invoke()
}

class RecommendMoviesUseCaseImpl @Inject constructor(
    private val repository: MovieRepository,
    private val notificationBuilder: NotificationBuilder,
) : RecommendMoviesUseCase {

    override suspend fun invoke() {
        val movieList = getRecommendedMovieList()
        if (movieList.isNotEmpty()) {
            notificationBuilder.showLegacyNotification(movieList)
        }
    }

    private suspend fun getRecommendedMovieList(): List<MovieModel> {
        return repository.updateAndGetNowMovieList().asSequence()
            .filter {
                it.theater.run {
                    cgv != null && lotte != null && megabox != null
                }
            }
            .filterIndexed { index, movie -> index < 3 || movie.isBest() }
            .take(6)
            .toList()
    }
}
