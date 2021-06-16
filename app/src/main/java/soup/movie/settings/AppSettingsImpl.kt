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
import androidx.preference.PreferenceManager
import kotlinx.coroutines.flow.Flow
import soup.movie.model.Theater
import soup.movie.settings.impl.AgeFilterPreference
import soup.movie.settings.impl.FavoriteTheaterPreference
import soup.movie.settings.impl.GenreFilterPreference
import soup.movie.settings.impl.StringPreference
import soup.movie.settings.impl.TheaterFilterPreference
import soup.movie.settings.model.AgeFilter
import soup.movie.settings.model.GenreFilter
import soup.movie.settings.model.TheaterFilter

class AppSettingsImpl(context: Context) : AppSettings {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    private val theaterFilterPref = TheaterFilterPreference(prefs, "theater_filter")
    override var theaterFilter by theaterFilterPref
    override fun getTheaterFilterFlow(): Flow<TheaterFilter> {
        return theaterFilterPref.asFlow()
    }

    private val ageFilterPref = AgeFilterPreference(prefs, "age_filter")
    override var ageFilter by ageFilterPref
    override fun getAgeFilterFlow(): Flow<AgeFilter> {
        return ageFilterPref.asFlow()
    }

    private val genreFilterPref = GenreFilterPreference(prefs, "favorite_genre")
    override var genreFilter by genreFilterPref
    override fun getGenreFilterFlow(): Flow<GenreFilter> {
        return genreFilterPref.asFlow()
    }

    private val themeOptionPref = StringPreference(prefs, "theme_option", "")
    override var themeOption by themeOptionPref
    override fun getThemeOptionFlow(): Flow<String> {
        return themeOptionPref.asFlow()
    }

    private val favoriteTheaterListPref = FavoriteTheaterPreference(prefs, "favorite_theaters")
    override var favoriteTheaterList by favoriteTheaterListPref
    override fun getFavoriteTheaterListFlow(): Flow<List<Theater>> {
        return favoriteTheaterListPref.asFlow()
    }
}
