package soup.movie.settings.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import soup.movie.settings.model.TheaterFilter
import kotlin.reflect.KProperty

class TheaterFilterPreference(
    private val preferences: SharedPreferences,
    private val name: String
) : Preference<TheaterFilter>(TheaterFilter(DEFAULT_VALUE)) {

    override fun getValue(thisRef: Any, property: KProperty<*>): TheaterFilter {
        return TheaterFilter(preferences.getInt(name, DEFAULT_VALUE))
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: TheaterFilter) {
        super.setValue(thisRef, property, value)
        preferences.edit {
            putInt(name, value.toFlags())
        }
    }

    companion object {

        private const val DEFAULT_VALUE = TheaterFilter.FLAG_THEATER_ALL
    }
}
