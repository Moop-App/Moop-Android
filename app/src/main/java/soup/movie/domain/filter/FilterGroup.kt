package soup.movie.domain.filter

import soup.movie.data.model.AgeFilter
import soup.movie.data.model.GenreFilter
import soup.movie.data.model.TheaterFilter

data class FilterGroup(
    val theaterFilter: TheaterFilter,
    val ageFilter: AgeFilter,
    val genreFilter: GenreFilter
)
