package soup.movie.settings.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import soup.movie.model.Theater
import soup.movie.settings.PrefSetting
import soup.movie.util.toJson
import timber.log.Timber

class TheatersSetting(
    preferences: SharedPreferences
) : PrefSetting<List<Theater>>(preferences) {

    override fun getDefaultValue(preferences: SharedPreferences): List<Theater> {
        return fromJson(preferences.getString(KEY, DEFAULT_VALUE) ?: DEFAULT_VALUE)
    }

    override fun saveValue(preferences: SharedPreferences, value: List<Theater>) {
        preferences.edit().putString(KEY, value.toJson()).apply()
    }

    companion object {

        private const val KEY = "favorite_theaters"
        private const val DEFAULT_VALUE = ""

        private val type = object : TypeToken<ArrayList<Theater>>() {}.type

        private fun fromJson(jsonStr: String): List<Theater> {
            return try {
                Gson().fromJson<List<Theater>>(jsonStr, type).orEmpty()
            } catch (t: Throwable) {
                Timber.w(t)
                emptyList()
            }
        }
    }
}
