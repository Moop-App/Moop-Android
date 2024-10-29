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

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import soup.movie.common.ApplicationScope
import soup.movie.common.IoDispatcher
import soup.movie.data.settings.AppSettings
import soup.movie.model.settings.AgeFilter
import soup.movie.model.settings.GenreFilter
import soup.movie.model.settings.TheaterFilter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettingsImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationScope private val coroutineScope: CoroutineScope,
) : AppSettings {

    private val Context.preferencesName: String
        get() = packageName + "_preferences"

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = context.preferencesName,
        produceMigrations = { context ->
            listOf(SharedPreferencesMigration(context, context.preferencesName))
        },
    )

    private val theaterFilterKey = intPreferencesKey("theater_filter")

    init {
        coroutineScope.launch {
            clearFavoriteTheaterList()
        }
    }

    override suspend fun setTheaterFilter(theaterFilter: TheaterFilter) {
        context.dataStore.edit { settings ->
            settings[theaterFilterKey] = theaterFilter.toFlags()
        }
    }

    override fun getTheaterFilterFlow(): Flow<TheaterFilter> {
        return context.dataStore.data.map { preferences ->
            TheaterFilter(
                preferences[theaterFilterKey]
                    ?: TheaterFilter.FLAG_THEATER_ALL,
            )
        }
    }

    private val ageFilterKey = intPreferencesKey("age_filter")

    override suspend fun setAgeFilter(ageFilter: AgeFilter) {
        context.dataStore.edit { settings ->
            settings[ageFilterKey] = ageFilter.toFlags()
        }
    }

    override fun getAgeFilterFlow(): Flow<AgeFilter> {
        return context.dataStore.data.map { preferences ->
            AgeFilter(
                preferences[ageFilterKey] ?: AgeFilter.FLAG_AGE_DEFAULT,
            )
        }
    }

    private val genreFilterKey = stringPreferencesKey("favorite_genre")

    override suspend fun setGenreFilter(genreFilter: GenreFilter) {
        context.dataStore.edit { settings ->
            settings[genreFilterKey] =
                genreFilter.blacklist.joinToString(separator = SEPARATOR)
        }
    }

    override fun getGenreFilterFlow(): Flow<GenreFilter> {
        return context.dataStore.data.map { preferences ->
            val genreString = preferences[genreFilterKey].orEmpty()
            GenreFilter(genreString.split(SEPARATOR).toSet())
        }
    }

    private val themeOptionKey = stringPreferencesKey("theme_option")

    override suspend fun setThemeOption(themeOption: String) {
        withContext(ioDispatcher) {
            context.dataStore.edit { settings ->
                settings[themeOptionKey] = themeOption
            }
        }
    }

    override suspend fun getThemeOption(): String {
        return getThemeOptionFlow().first()
    }

    override fun getThemeOptionFlow(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[themeOptionKey].orEmpty()
        }
    }

    private val favoriteTheaterListKey = stringPreferencesKey("favorite_theaters")

    private suspend fun clearFavoriteTheaterList() {
        withContext(ioDispatcher) {
            context.dataStore.edit { settings ->
                settings.remove(favoriteTheaterListKey)
            }
        }
    }

    companion object {

        private const val SEPARATOR = "|"
    }
}
