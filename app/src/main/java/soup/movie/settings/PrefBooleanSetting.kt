package soup.movie.settings

import android.content.SharedPreferences

abstract class PrefBooleanSetting(
    preferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Boolean
) : PrefSetting<Boolean>(preferences) {

    final override fun getDefaultValue(preferences: SharedPreferences): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    final override fun saveValue(preferences: SharedPreferences, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }
}
