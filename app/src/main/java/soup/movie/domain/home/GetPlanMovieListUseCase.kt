package soup.movie.domain.home

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import soup.movie.data.repository.MoopRepository
import soup.movie.domain.home.model.HomeDomainModel
import soup.movie.domain.model.MovieFilter
import soup.movie.domain.model.getDDay
import soup.movie.model.Movie

class GetPlanMovieListUseCase(
    private val repository: MoopRepository
) {

    operator fun invoke(
        movieFilter: MovieFilter
    ): Flow<HomeDomainModel> {
        return repository.getPlanMovieList()
            .map { list ->
                list.asSequence()
                    .sortedBy(Movie::getDDay)
                    .filter { movieFilter(it) }
                    .toList()
            }
            .flowOn(Dispatchers.Default)
            .catch { emit(emptyList()) }
            .map { HomeDomainModel(it) }
    }
}
