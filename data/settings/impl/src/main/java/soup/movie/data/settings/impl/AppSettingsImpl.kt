/*
 * Copyright 2022 SOUP
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
package soup.movie.data.settings.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import soup.movie.common.ApplicationScope
import soup.movie.data.settings.AppSettings
import soup.movie.model.settings.AgeFilter
import soup.movie.model.settings.TheaterFilter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettingsImpl @Inject constructor(
    private val preferences: DataStore<Preferences>,
    @ApplicationScope private val coroutineScope: CoroutineScope,
) : AppSettings {

    private val theaterFilterKey = intPreferencesKey("theater_filter")

    init {
        coroutineScope.launch {
            clearStaleData()
        }
    }

    override suspend fun setTheaterFilter(theaterFilter: TheaterFilter) {
        preferences.edit { settings ->
            settings[theaterFilterKey] = theaterFilter.toFlags()
        }
    }

    override fun getTheaterFilterFlow(): Flow<TheaterFilter> {
        return preferences.data.map { preferences ->
            TheaterFilter(
                preferences[theaterFilterKey]
                    ?: TheaterFilter.FLAG_THEATER_ALL,
            )
        }
    }

    private val ageFilterKey = intPreferencesKey("age_filter")

    override suspend fun setAgeFilter(ageFilter: AgeFilter) {
        preferences.edit { settings ->
            settings[ageFilterKey] = ageFilter.toFlags()
        }
    }

    override fun getAgeFilterFlow(): Flow<AgeFilter> {
        return preferences.data.map { preferences ->
            AgeFilter(
                preferences[ageFilterKey] ?: AgeFilter.FLAG_AGE_DEFAULT,
            )
        }
    }

    private val themeOptionKey = stringPreferencesKey("theme_option")

    override suspend fun setThemeOption(themeOption: String) {
        preferences.edit { settings ->
            settings[themeOptionKey] = themeOption
        }
    }

    override suspend fun getThemeOption(): String {
        return getThemeOptionFlow().first()
    }

    override fun getThemeOptionFlow(): Flow<String> {
        return preferences.data.map { preferences ->
            preferences[themeOptionKey].orEmpty()
        }
    }

    private suspend fun clearStaleData() {
        preferences.edit { settings ->
            settings.remove(stringPreferencesKey("favorite_theaters"))
            settings.remove(stringPreferencesKey("favorite_genre"))
        }
    }
}
