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
package soup.movie.data.settings

import kotlinx.coroutines.flow.Flow
import soup.movie.model.Theater
import soup.movie.model.settings.AgeFilter
import soup.movie.model.settings.GenreFilter
import soup.movie.model.settings.TheaterFilter

interface AppSettings {

    suspend fun setTheaterFilter(theaterFilter: TheaterFilter)
    fun getTheaterFilterFlow(): Flow<TheaterFilter>

    suspend fun setAgeFilter(ageFilter: AgeFilter)
    fun getAgeFilterFlow(): Flow<AgeFilter>

    suspend fun setGenreFilter(genreFilter: GenreFilter)
    fun getGenreFilterFlow(): Flow<GenreFilter>

    suspend fun setThemeOption(themeOption: String)
    suspend fun getThemeOption(): String
    fun getThemeOptionFlow(): Flow<String>

    suspend fun setFavoriteTheaterList(list: List<Theater>)
    suspend fun getFavoriteTheaterList(): List<Theater>
    fun getFavoriteTheaterListFlow(): Flow<List<Theater>>
}
