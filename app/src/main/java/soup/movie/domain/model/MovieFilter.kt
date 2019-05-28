package soup.movie.domain.model

import soup.movie.data.model.AgeFilter
import soup.movie.data.model.GenreFilter
import soup.movie.data.model.Movie
import soup.movie.data.model.TheaterFilter

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
        fun isScreeningAtCgv(): Boolean = cgv != null
        fun isScreeningAtLotteCinema(): Boolean = lotte != null
        fun isScreeningAtMegabox(): Boolean = megabox != null
        return theaterFilter.hasCgv() and isScreeningAtCgv()
            || theaterFilter.hasLotteCinema() and isScreeningAtLotteCinema()
            || theaterFilter.hasMegabox() and isScreeningAtMegabox()
    }

    private fun Movie.isFilterBy(ageFilter: AgeFilter): Boolean {
        fun isScreeningForAgeAll(): Boolean = ageValue < 12
        fun isScreeningOverAge12(): Boolean = (12 <= ageValue) and (ageValue < 15)
        fun isScreeningOverAge15(): Boolean = (15 <= ageValue) and (ageValue < 19)
        fun isScreeningOverAge19(): Boolean = 19 <= ageValue
        return (ageFilter.hasAll() and isScreeningForAgeAll())
            || (ageFilter.has12() and isScreeningOverAge12())
            || (ageFilter.has15() and isScreeningOverAge15())
            || (ageFilter.has19() and isScreeningOverAge19())
    }

    private fun Movie.isFilterBy(genreFilter: GenreFilter): Boolean {
        return genre?.any { it !in genreFilter.blacklist }
            ?: genre.isNullOrEmpty() and genreFilter.isEtcIncluded()
    }
}
