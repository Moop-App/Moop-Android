package soup.movie.domain.main

import io.reactivex.Observable
import soup.movie.data.MoopRepository
import soup.movie.data.model.AgeFilter
import soup.movie.data.model.GenreFilter
import soup.movie.data.model.Movie
import soup.movie.data.model.TheaterFilter
import soup.movie.domain.model.MovieFilter
import soup.movie.ui.main.movie.MovieListUiModel

class GetNowMovieUseCase(
    private val repository: MoopRepository
) {

    fun asObservable(
        clearCache: Boolean,
        movieFilter: MovieFilter
    ): Observable<MovieListUiModel> {
        return repository.getNowList(clearCache)
            .map { it.list.sortedBy(Movie::rank) }
            .map { it ->
                it.asSequence()
                    .filter { it.isFilterBy(movieFilter.theaterFilter) }
                    .filter { it.isFilterBy(movieFilter.ageFilter) }
                    .filter { it.isFilterBy(movieFilter.genreFilter) }
                    .toList()
            }
            .map { MovieListUiModel.DoneState(it) as MovieListUiModel }
            .startWith(MovieListUiModel.LoadingState)
            .onErrorReturnItem(MovieListUiModel.ErrorState)
    }

    private fun Movie.isFilterBy(theaterFilter: TheaterFilter): Boolean {
        return (theaterFilter.hasCgv() and isScreeningAtCgv()) or
            (theaterFilter.hasLotteCinema() and isScreeningAtLotteCinema()) or
            (theaterFilter.hasMegabox() and isScreeningAtMegabox())
    }

    private fun Movie.isFilterBy(ageFilter: AgeFilter): Boolean {
        return (ageFilter.hasAll() and isScreeningForAgeAll()) or
            (ageFilter.has12() and isScreeningOverAge12()) or
            (ageFilter.has15() and isScreeningOverAge15()) or
            (ageFilter.has19() and isScreeningOverAge19())
    }

    private fun Movie.isFilterBy(genreFilter: GenreFilter): Boolean {
        return genre?.any { it !in genreFilter.blacklist }
            ?: genre.isNullOrEmpty() and genreFilter.isEtcIncluded()
    }
}
