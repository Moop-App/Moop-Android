package soup.movie.domain.model

import soup.movie.data.model.AgeFilter
import soup.movie.data.model.GenreFilter
import soup.movie.data.model.TheaterFilter

class MovieFilter(
    val theaterFilter: TheaterFilter,
    val ageFilter: AgeFilter,
    val genreFilter: GenreFilter
)
