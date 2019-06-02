package soup.movie.domain.home

import io.reactivex.Observable
import soup.movie.data.MoopRepository
import soup.movie.data.helper.getDDay
import soup.movie.data.model.Movie
import soup.movie.domain.model.MovieFilter
import soup.movie.ui.home.HomeUiModel

class GetPlanMovieListUseCase(
    private val repository: MoopRepository
) {

    operator fun invoke(
        clearCache: Boolean,
        movieFilter: MovieFilter
    ): Observable<HomeUiModel> {
        return repository.getPlanList(clearCache)
            .map { response ->
                response.list.asSequence()
                    .sortedBy(Movie::getDDay)
                    .filter { movieFilter(it) }
                    .toList()
            }
            .map { HomeUiModel.DoneState(it) as HomeUiModel }
            .startWith(HomeUiModel.LoadingState)
            .onErrorReturnItem(HomeUiModel.ErrorState)
    }
}
