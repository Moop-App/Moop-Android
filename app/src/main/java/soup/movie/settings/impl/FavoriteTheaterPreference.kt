package soup.movie.settings.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import soup.movie.model.Theater
import soup.movie.util.toJson
import timber.log.Timber
import kotlin.reflect.KProperty

class FavoriteTheaterPreference(
    private val preferences: SharedPreferences,
    private val name: String
) : Preference<List<Theater>>(emptyList()) {

    override fun getValue(thisRef: Any, property: KProperty<*>): List<Theater> {
        return fromJson(preferences.getString(name, "") ?: "")
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: List<Theater>) {
        super.setValue(thisRef, property, value)
        preferences.edit {
            putString(name, value.toJson())
        }
    }

    companion object {

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
