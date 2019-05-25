package soup.movie.settings.impl

import android.content.SharedPreferences
import soup.movie.settings.PrefSetting

class GenreFilterSetting(
    preferences: SharedPreferences
) : PrefSetting<Set<String>>(preferences) {

    override fun getDefaultValue(preferences: SharedPreferences): Set<String> {
        return (preferences.getString(KEY, DEFAULT_VALUE) ?: DEFAULT_VALUE).split(SEPARATOR).toSet()
    }

    override fun saveValue(preferences: SharedPreferences, value: Set<String>) {
        preferences.edit().putString(KEY, value.joinToString(separator = SEPARATOR)).apply()
    }

    companion object {

        private const val KEY = "favorite_genre"
        private const val DEFAULT_VALUE = ""
        private const val SEPARATOR = "|"
    }
}
