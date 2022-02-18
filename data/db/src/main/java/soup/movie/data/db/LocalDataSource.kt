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
package soup.movie.data.db

import kotlinx.coroutines.flow.Flow
import soup.movie.model.Movie
import soup.movie.model.MovieList
import soup.movie.model.OpenDateAlarm
import soup.movie.model.TheaterAreaGroup

interface LocalDataSource {

    suspend fun saveNowMovieList(movieList: MovieList)
    fun getNowMovieListFlow(): Flow<List<Movie>>
    suspend fun savePlanMovieList(movieList: MovieList)
    fun getPlanMovieListFlow(): Flow<List<Movie>>
    suspend fun getNowLastUpdateTime(): Long
    suspend fun getPlanLastUpdateTime(): Long
    suspend fun getAllMovieList(): List<Movie>
    suspend fun getNowMovieList(): List<Movie>

    fun saveCodeList(response: TheaterAreaGroup)
    fun getCodeList(): TheaterAreaGroup?

    suspend fun addFavoriteMovie(movie: Movie)
    suspend fun removeFavoriteMovie(movieId: String)
    fun getFavoriteMovieList(): Flow<List<Movie>>
    suspend fun isFavoriteMovie(movieId: String): Boolean

    suspend fun getOpenDateAlarmListUntil(date: String): List<OpenDateAlarm>
    suspend fun hasOpenDateAlarms(): Boolean
    suspend fun insertOpenDateAlarm(alarm: OpenDateAlarm)
    suspend fun deleteOpenDateAlarms(alarms: List<OpenDateAlarm>)
}
