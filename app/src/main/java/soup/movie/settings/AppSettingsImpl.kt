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
package soup.movie.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import soup.movie.feature.common.settings.AppSettings
import soup.movie.model.Theater
import soup.movie.model.TheaterType
import soup.movie.feature.common.settings.model.AgeFilter
import soup.movie.feature.common.settings.model.GenreFilter
import soup.movie.feature.common.settings.model.TheaterFilter

class AppSettingsImpl(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) : AppSettings {

    private val Context.preferencesName: String
        get() = packageName + "_preferences"

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = context.preferencesName,
        produceMigrations = { context ->
            listOf(SharedPreferencesMigration(context, context.preferencesName))
        }
    )

    private val theaterFilterKey = intPreferencesKey("theater_filter")

    override suspend fun setTheaterFilter(theaterFilter: TheaterFilter) {
        context.dataStore.edit { settings ->
            settings[theaterFilterKey] = theaterFilter.toFlags()
        }
    }

    override fun getTheaterFilterFlow(): Flow<TheaterFilter> {
        return context.dataStore.data.map { preferences ->
            TheaterFilter(preferences[theaterFilterKey] ?: TheaterFilter.FLAG_THEATER_ALL)
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
            AgeFilter(preferences[ageFilterKey] ?: AgeFilter.FLAG_AGE_DEFAULT)
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

    override suspend fun setFavoriteTheaterList(list: List<Theater>) {
        withContext(ioDispatcher) {
            context.dataStore.edit { settings ->
                val rawList = list.map { it.toRaw() }
                settings[favoriteTheaterListKey] = Json.encodeToString(rawList)
            }
        }
    }

    override suspend fun getFavoriteTheaterList(): List<Theater> {
        return getFavoriteTheaterListFlow().first()
    }

    override fun getFavoriteTheaterListFlow(): Flow<List<Theater>> {
        return context.dataStore.data.map { preferences ->
            val string = preferences[favoriteTheaterListKey]
            if (string != null) {
                val rawList: List<RawTheater> = Json.decodeFromString(string)
                rawList.map { it.toModel() }
            } else {
                emptyList()
            }
        }
    }

    companion object {

        private const val SEPARATOR = "|"
    }
}

@Serializable
private data class RawTheater(
    val id: String,
    val type: String,
    val code: String,
    val name: String,
    val lng: Double,
    val lat: Double,
) {
    fun toModel() = Theater(
        id = id,
        type = TheaterType.valueOf(type),
        code = code,
        name = name,
        lng = lng,
        lat = lat,
    )
}

private fun Theater.toRaw() = RawTheater(
    id = id,
    type = type.name,
    code = code,
    name = name,
    lng = lng,
    lat = lat,
)
