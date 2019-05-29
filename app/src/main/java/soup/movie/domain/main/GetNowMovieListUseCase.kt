package soup.movie.domain.main

import io.reactivex.Observable
import soup.movie.data.MoopRepository
import soup.movie.data.model.Movie
import soup.movie.domain.model.MovieFilter
import soup.movie.ui.main.movie.MovieListUiModel

class GetNowMovieListUseCase(
    private val repository: MoopRepository
) {

    operator fun invoke(
        clearCache: Boolean,
        movieFilter: MovieFilter
    ): Observable<MovieListUiModel> {
        return repository.getNowList(clearCache)
            .map { it ->
                it.list.asSequence()
                    .sortedBy(Movie::rank)
                    .filter { movieFilter(it) }
                    .toList()
            }
            .map { MovieListUiModel.DoneState(it) as MovieListUiModel }
            .startWith(MovieListUiModel.LoadingState)
            .onErrorReturnItem(MovieListUiModel.ErrorState)
    }
}
