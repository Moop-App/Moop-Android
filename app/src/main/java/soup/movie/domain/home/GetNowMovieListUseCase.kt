package soup.movie.domain.home

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import soup.movie.data.MoopRepository
import soup.movie.data.model.Movie
import soup.movie.domain.home.model.HomeDomainModel
import soup.movie.domain.model.MovieFilter

class GetNowMovieListUseCase(
    private val repository: MoopRepository
) {

    operator fun invoke(
        movieFilter: MovieFilter
    ): Observable<HomeDomainModel> {
        return repository.getNowList()
            .observeOn(Schedulers.computation())
            .map { response ->
                response.list.asSequence()
                    .sortedBy(Movie::rank)
                    .filter { movieFilter(it) }
                    .toList()
            }
            .onErrorReturnItem(emptyList())
            .map { HomeDomainModel(it) }
    }
}
