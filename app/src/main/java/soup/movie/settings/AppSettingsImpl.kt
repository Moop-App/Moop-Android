package soup.movie.settings

import android.content.Context
import androidx.preference.PreferenceManager
import kotlinx.coroutines.flow.Flow
import soup.movie.model.Theater
import soup.movie.settings.impl.*
import soup.movie.settings.model.AgeFilter
import soup.movie.settings.model.GenreFilter
import soup.movie.settings.model.TheaterFilter

class AppSettingsImpl(context: Context) : AppSettings {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    private val theaterFilterPref = TheaterFilterPreference(prefs, "theater_filter")
    override var theaterFilter by theaterFilterPref
    override fun getTheaterFilterFlow(): Flow<TheaterFilter> {
        return theaterFilterPref.asFlow()
    }

    private val ageFilterPref = AgeFilterPreference(prefs, "age_filter")
    override var ageFilter by ageFilterPref
    override fun getAgeFilterFlow(): Flow<AgeFilter> {
        return ageFilterPref.asFlow()
    }

    private val genreFilterPref = GenreFilterPreference(prefs, "favorite_genre")
    override var genreFilter by genreFilterPref
    override fun getGenreFilterFlow(): Flow<GenreFilter> {
        return genreFilterPref.asFlow()
    }

    private val themeOptionPref = StringPreference(prefs, "theme_option", "")
    override var themeOption by themeOptionPref
    override fun getThemeOptionFlow(): Flow<String> {
        return themeOptionPref.asFlow()
    }

    private val favoriteTheaterListPref = FavoriteTheaterPreference(prefs, "favorite_theaters")
    override var favoriteTheaterList by favoriteTheaterListPref
    override fun getFavoriteTheaterListFlow(): Flow<List<Theater>> {
        return favoriteTheaterListPref.asFlow()
    }
}
