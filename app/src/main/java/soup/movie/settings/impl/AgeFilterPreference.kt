package soup.movie.settings.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import soup.movie.settings.model.AgeFilter
import kotlin.reflect.KProperty

class AgeFilterPreference(
    private val preferences: SharedPreferences,
    private val name: String
) : Preference<AgeFilter>(AgeFilter(AgeFilter.FLAG_AGE_DEFAULT)) {

    override fun getValue(thisRef: Any, property: KProperty<*>): AgeFilter {
        return AgeFilter(preferences.getInt(name, AgeFilter.FLAG_AGE_DEFAULT))
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: AgeFilter) {
        super.setValue(thisRef, property, value)
        preferences.edit {
            putInt(name, value.toFlags())
        }
    }
}
