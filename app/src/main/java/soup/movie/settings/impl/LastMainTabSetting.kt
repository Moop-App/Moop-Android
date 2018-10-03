package soup.movie.settings.impl

import android.content.SharedPreferences
import soup.movie.settings.PrefSetting
import soup.movie.settings.impl.LastMainTabSetting.Tab

class LastMainTabSetting(preferences: SharedPreferences) :
        PrefSetting<Tab>(preferences) {

    enum class Tab {
        Now, Plan, Theaters, Settings
    }

    override fun getDefaultValue(preferences: SharedPreferences): Tab =
            preferences.getString(KEY, DEFAULT_VALUE.toString())
                    ?.let { Tab.valueOf(it) }
                    ?: DEFAULT_VALUE

    override fun saveValue(preferences: SharedPreferences, value: Tab) =
            preferences.edit().putString(KEY, value.toString()).apply()

    companion object {

        private const val KEY = "last_main_tab"
        private val DEFAULT_VALUE = Tab.Now
    }
}
