package soup.movie.domain.main

import io.reactivex.Observable
import soup.movie.data.MoopRepository
import soup.movie.data.helper.getDDay
import soup.movie.data.model.Movie
import soup.movie.domain.model.MovieFilter
import soup.movie.ui.main.movie.MovieListUiModel

class GetPlanMovieUseCase(
    private val repository: MoopRepository
) {

    fun asObservable(
        clearCache: Boolean,
        movieFilter: MovieFilter
    ): Observable<MovieListUiModel> {
        return repository.getPlanList(clearCache)
            .map { response ->
                response.list.asSequence()
                    .sortedBy(Movie::getDDay)
                    .filter { movieFilter(it) }
                    .toList()
            }
            .map { MovieListUiModel.DoneState(it) as MovieListUiModel }
            .startWith(MovieListUiModel.LoadingState)
            .onErrorReturnItem(MovieListUiModel.ErrorState)
    }
}
