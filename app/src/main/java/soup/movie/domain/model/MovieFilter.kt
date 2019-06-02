package soup.movie.domain.model

import soup.movie.settings.model.AgeFilter
import soup.movie.settings.model.GenreFilter
import soup.movie.data.model.Movie
import soup.movie.settings.model.TheaterFilter

class MovieFilter(
    private val theaterFilter: TheaterFilter,
    private val ageFilter: AgeFilter,
    private val genreFilter: GenreFilter
) {

    operator fun invoke(movie: Movie): Boolean {
        return movie.isFilterBy(theaterFilter)
            && movie.isFilterBy(ageFilter)
            && movie.isFilterBy(genreFilter)
    }

    private fun Movie.isFilterBy(theaterFilter: TheaterFilter): Boolean {
        val isScreeningAtCgv = cgv != null
        val isScreeningAtLotteCinema = lotte != null
        val isScreeningAtMegabox = megabox != null
        return theaterFilter.hasCgv() && isScreeningAtCgv
            || theaterFilter.hasLotteCinema() && isScreeningAtLotteCinema
            || theaterFilter.hasMegabox() && isScreeningAtMegabox
    }

    private fun Movie.isFilterBy(ageFilter: AgeFilter): Boolean {
        val isScreeningForAgeAll = ageValue < 12
        val isScreeningOverAge12 = (12 <= ageValue) && (ageValue < 15)
        val isScreeningOverAge15 = (15 <= ageValue) && (ageValue < 19)
        val isScreeningOverAge19 = 19 <= ageValue
        return (ageFilter.hasAll() && isScreeningForAgeAll)
            || (ageFilter.has12() && isScreeningOverAge12)
            || (ageFilter.has15() && isScreeningOverAge15)
            || (ageFilter.has19() && isScreeningOverAge19)
    }

    private fun Movie.isFilterBy(genreFilter: GenreFilter): Boolean {
        return genre?.any { it !in genreFilter.blacklist } ?: false
    }
}
