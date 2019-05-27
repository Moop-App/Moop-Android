package soup.movie.settings.impl

import android.content.SharedPreferences
import soup.movie.theme.ThemeOptionManager.Companion.defaultThemeOption
import soup.movie.theme.ThemeOption
import soup.movie.settings.PrefSetting

class ThemeOptionSetting(
    preferences: SharedPreferences
) : PrefSetting<ThemeOption>(preferences) {

    override fun getDefaultValue(preferences: SharedPreferences): ThemeOption {
        return ThemeOption.valueOf(
            preferences.getString(KEY, defaultThemeOption.toString()) ?: defaultThemeOption.toString()
        )
    }

    override fun saveValue(preferences: SharedPreferences, value: ThemeOption) {
        preferences.edit().putString(KEY, value.toString()).apply()
    }

    companion object {

        private const val KEY = "theme_option"
    }
}
