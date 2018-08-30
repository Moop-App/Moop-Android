package soup.movie.settings

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import soup.movie.data.model.Theater

class TheaterSetting(preferences: SharedPreferences) :
        PrefSetting<List<Theater>>(preferences) {

    override fun getDefaultValue(preferences: SharedPreferences): List<Theater> {
        return fromJson(preferences.getString(KEY, DEFAULT_VALUE))
    }

    override fun saveValue(preferences: SharedPreferences, value: List<Theater>) {
        return preferences.edit().putString(KEY, toJson(value)).apply()
    }

    companion object {

        private const val KEY = "favorite_theaters"
        private const val DEFAULT_VALUE = ""

        private val type = object : TypeToken<ArrayList<Theater>>() {}.type

        private fun toJson(theaterCodeList: List<Theater>): String {
            return Gson().toJson(theaterCodeList)
        }

        private fun fromJson(jsonStr: String): List<Theater> {
            return Gson().fromJson<List<Theater>>(jsonStr, type).orEmpty()
        }
    }
}
