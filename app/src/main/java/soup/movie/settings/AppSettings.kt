package soup.movie.settings

import kotlinx.coroutines.flow.Flow
import soup.movie.model.Theater
import soup.movie.settings.model.AgeFilter
import soup.movie.settings.model.GenreFilter
import soup.movie.settings.model.TheaterFilter

interface AppSettings {

    var theaterFilter: TheaterFilter
    fun getTheaterFilterFlow(): Flow<TheaterFilter>

    var ageFilter: AgeFilter
    fun getAgeFilterFlow(): Flow<AgeFilter>

    var genreFilter: GenreFilter
    fun getGenreFilterFlow(): Flow<GenreFilter>

    var themeOption: String
    fun getThemeOptionFlow(): Flow<String>

    var favoriteTheaterList: List<Theater>
    fun getFavoriteTheaterListFlow(): Flow<List<Theater>>
}
