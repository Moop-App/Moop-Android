package soup.movie.settings.impl

import android.content.SharedPreferences
import soup.movie.settings.PrefSetting

class UseWebLinkSetting(preferences: SharedPreferences) :
        PrefSetting<Boolean>(preferences) {

    override fun getDefaultValue(preferences: SharedPreferences): Boolean {
        return preferences.getBoolean(KEY, true)
    }

    override fun saveValue(preferences: SharedPreferences, value: Boolean) {
        return preferences.edit().putBoolean(KEY, value).apply()
    }

    companion object {

        private const val KEY = "use_web_link"
    }
}
