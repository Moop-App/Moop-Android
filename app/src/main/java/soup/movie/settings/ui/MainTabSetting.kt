package soup.movie.settings.ui

import android.content.SharedPreferences
import soup.movie.settings.PrefSetting
import soup.movie.settings.ui.MainTabSetting.Tab

class MainTabSetting(preferences: SharedPreferences) :
        PrefSetting<Tab>(preferences) {

    enum class Tab {
        Now, Plan, Settings
    }

    override fun getDefaultValue(preferences: SharedPreferences): Tab {
        return Tab.valueOf(preferences.getString(KEY, DEFAULT_VALUE.toString()))
    }

    override fun saveValue(preferences: SharedPreferences, value: Tab) {
        return preferences.edit().putString(KEY, value.toString()).apply()
    }

    companion object {

        private const val KEY = "main_tab"
        private val DEFAULT_VALUE = Tab.Now
    }
}
