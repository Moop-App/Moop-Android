package soup.movie.home.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import soup.movie.settings.AppSettings

fun AppSettings.getMovieFilterFlow(): Flow<MovieFilter> {
    return combine(
        getTheaterFilterFlow(),
        getAgeFilterFlow(),
        getGenreFilterFlow(),
        transform = { theaterFilter, ageFilter, genreFilter ->
            MovieFilter(
                theaterFilter,
                ageFilter,
                genreFilter
            )
        }
    )
}
