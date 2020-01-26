package soup.movie.domain.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
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
            ageFilterSetting.asFlow().distinctUntilChanged(),
            genreFilterSetting.asFlow()
        ) { theaterFilter, ageFilter, genreFilter ->
            MovieFilter(
                theaterFilter,
                ageFilter,
                genreFilter
            )
        }
    }
}
