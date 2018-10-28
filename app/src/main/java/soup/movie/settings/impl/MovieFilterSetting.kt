package soup.movie.settings.impl

import android.content.SharedPreferences
import soup.movie.data.model.MovieFilter
import soup.movie.settings.PrefSetting

class MovieFilterSetting(preferences: SharedPreferences) :
        PrefSetting<MovieFilter>(preferences) {

    override fun getDefaultValue(preferences: SharedPreferences): MovieFilter =
            MovieFilter(preferences.getInt(KEY, DEFAULT_VALUE))

    override fun saveValue(preferences: SharedPreferences, value: MovieFilter) =
            preferences.edit().putInt(KEY, value.toFlags()).apply()

    companion object {

        private const val KEY = "movie_filter"
        private const val DEFAULT_VALUE: Int = MovieFilter.FLAG_THEATER_ALL
    }
}
