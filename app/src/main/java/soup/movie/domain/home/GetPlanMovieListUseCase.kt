package soup.movie.domain.home

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import soup.movie.data.repository.MoopRepository
import soup.movie.data.model.Movie
import soup.movie.domain.home.model.HomeDomainModel
import soup.movie.domain.model.MovieFilter
import soup.movie.domain.model.getDDay

class GetPlanMovieListUseCase(
    private val repository: MoopRepository
) {

    operator fun invoke(
        movieFilter: MovieFilter
    ): Observable<HomeDomainModel> {
        return repository.getPlanList()
            .observeOn(Schedulers.computation())
            .map { list ->
                list.asSequence()
                    .sortedBy(Movie::getDDay)
                    .filter { movieFilter(it) }
                    .toList()
            }
            .onErrorReturnItem(emptyList())
            .map { HomeDomainModel(it) }
    }
}
