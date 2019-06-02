package soup.movie.settings.impl

import android.content.SharedPreferences
import soup.movie.settings.PrefSetting
import soup.movie.settings.model.GenreFilter

class GenreFilterSetting(
    preferences: SharedPreferences
) : PrefSetting<GenreFilter>(preferences) {

    override fun getDefaultValue(preferences: SharedPreferences): GenreFilter {
        val genreString = preferences.getString(KEY, DEFAULT_VALUE) ?: DEFAULT_VALUE
        return GenreFilter(genreString.split(SEPARATOR).toSet())
    }

    override fun saveValue(preferences: SharedPreferences, value: GenreFilter) {
        preferences.edit()
            .putString(KEY, value.blacklist.joinToString(separator = SEPARATOR))
            .apply()
    }

    companion object {

        private const val KEY = "favorite_genre"
        private const val DEFAULT_VALUE = ""
        private const val SEPARATOR = "|"
    }
}
