package soup.movie.settings.impl

import android.content.SharedPreferences
import soup.movie.settings.PrefSetting

class ThemeOptionSetting(
    preferences: SharedPreferences
) : PrefSetting<String>(preferences) {

    override fun getDefaultValue(preferences: SharedPreferences): String {
        return preferences.getString(KEY, "") ?: ""
    }

    override fun saveValue(preferences: SharedPreferences, value: String) {
        preferences.edit().putString(KEY, value).apply()
    }

    companion object {

        private const val KEY = "theme_option"
    }
}
