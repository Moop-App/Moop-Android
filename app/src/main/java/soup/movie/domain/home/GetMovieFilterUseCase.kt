package soup.movie.domain.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import soup.movie.domain.model.MovieFilter
import soup.movie.settings.impl.AgeFilterSetting
import soup.movie.settings.impl.GenreFilterSetting
import soup.movie.settings.impl.TheaterFilterSetting

class GetMovieFilterUseCase(
    private val theaterFilterSetting: TheaterFilterSetting,
    private val ageFilterSetting: AgeFilterSetting,
    private val genreFilterSetting: GenreFilterSetting
) {

    operator fun invoke(): Flow<MovieFilter> {
        return combine(
            theaterFilterSetting.asFlow(),
            ageFilterSetting.asFlow(),
            genreFilterSetting.asFlow(),
            transform = { theaterFilter, ageFilter, genreFilter ->
                MovieFilter(theaterFilter, ageFilter, genreFilter)
            }
        )
    }
}
