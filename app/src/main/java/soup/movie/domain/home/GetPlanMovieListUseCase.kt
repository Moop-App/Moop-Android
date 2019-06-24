package soup.movie.domain.home

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import soup.movie.data.MoopRepository
import soup.movie.data.model.Movie
import soup.movie.domain.Result
import soup.movie.domain.ResultMapper
import soup.movie.domain.home.model.HomeDomainModel
import soup.movie.domain.model.MovieFilter
import soup.movie.domain.model.getDDay

class GetPlanMovieListUseCase(
    private val repository: MoopRepository
) : ResultMapper {

    operator fun invoke(
        movieFilter: MovieFilter,
        clearCache: Boolean
    ): Observable<Result<HomeDomainModel>> {
        return repository.getPlanList(clearCache)
            .observeOn(Schedulers.computation())
            .map { response ->
                HomeDomainModel(
                    response.list.asSequence()
                        .sortedBy(Movie::getDDay)
                        .filter { movieFilter(it) }
                        .toList()
                )
            }
            .mapResult()
    }
}
