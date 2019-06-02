package soup.movie.settings.impl

import android.content.SharedPreferences
import soup.movie.settings.model.TheaterFilter
import soup.movie.settings.PrefSetting

class TheaterFilterSetting(
    preferences: SharedPreferences
) : PrefSetting<TheaterFilter>(preferences) {

    override fun getDefaultValue(preferences: SharedPreferences): TheaterFilter {
        return TheaterFilter(preferences.getInt(KEY, DEFAULT_VALUE))
    }

    override fun saveValue(preferences: SharedPreferences, value: TheaterFilter) {
        preferences.edit().putInt(KEY, value.toFlags()).apply()
    }

    companion object {

        private const val KEY = "theater_filter"
        private const val DEFAULT_VALUE: Int = TheaterFilter.FLAG_THEATER_ALL
    }
}
