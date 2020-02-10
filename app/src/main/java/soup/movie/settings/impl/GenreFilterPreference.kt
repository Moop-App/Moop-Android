package soup.movie.settings.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import soup.movie.settings.model.GenreFilter
import kotlin.reflect.KProperty

class GenreFilterPreference(
    private val preferences: SharedPreferences,
    private val name: String
) : Preference<GenreFilter>(GenreFilter(emptySet())) {

    override fun getValue(thisRef: Any, property: KProperty<*>): GenreFilter {
        val genreString = preferences.getString(name, DEFAULT_VALUE) ?: DEFAULT_VALUE
        return GenreFilter(genreString.split(SEPARATOR).toSet())
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: GenreFilter) {
        super.setValue(thisRef, property, value)
        preferences.edit {
            putString(name, value.blacklist.joinToString(separator = SEPARATOR))
        }
    }

    companion object {

        private const val DEFAULT_VALUE = ""
        private const val SEPARATOR = "|"
    }
}
