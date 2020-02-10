package soup.movie.domain.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import soup.movie.domain.model.MovieFilter
import soup.movie.settings.AppSettings

class GetMovieFilterUseCase(
    private val appSettings: AppSettings
) {

    operator fun invoke(): Flow<MovieFilter> {
        return combine(
            appSettings.getTheaterFilterFlow(),
            appSettings.getAgeFilterFlow(),
            appSettings.getGenreFilterFlow(),
            transform = { theaterFilter, ageFilter, genreFilter ->
                MovieFilter(theaterFilter, ageFilter, genreFilter)
            }
        )
    }
}
