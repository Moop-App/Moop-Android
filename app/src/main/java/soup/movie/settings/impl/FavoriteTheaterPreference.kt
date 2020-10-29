package soup.movie.settings.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import soup.movie.model.Theater
import kotlin.reflect.KProperty

class FavoriteTheaterPreference(
    private val preferences: SharedPreferences,
    private val name: String
) : Preference<List<Theater>>(emptyList()) {

    override fun getValue(thisRef: Any, property: KProperty<*>): List<Theater> {
        val string = preferences.getString(name, null) ?: return emptyList()
        return Json.decodeFromString(string)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: List<Theater>) {
        super.setValue(thisRef, property, value)
        preferences.edit {
            putString(name, Json.encodeToString(value))
        }
    }
}
