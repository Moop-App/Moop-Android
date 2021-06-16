/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
