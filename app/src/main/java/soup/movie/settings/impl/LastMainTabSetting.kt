package soup.movie.settings.impl

import android.content.SharedPreferences
import soup.movie.settings.PrefSetting
import soup.movie.settings.impl.LastMainTabSetting.Tab

class LastMainTabSetting(
    preferences: SharedPreferences
) : PrefSetting<Tab>(preferences) {

    enum class Tab {
        Now, Plan
    }

    override fun getDefaultValue(preferences: SharedPreferences): Tab = Tab.Now

    override fun saveValue(preferences: SharedPreferences, value: Tab) {}
}
