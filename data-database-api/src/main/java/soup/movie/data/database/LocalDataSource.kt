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
package soup.movie.data.database

import kotlinx.coroutines.flow.Flow
import soup.movie.model.MovieListModel
import soup.movie.model.MovieModel
import soup.movie.model.OpenDateAlarmModel
import soup.movie.model.TheaterAreaGroupModel

interface LocalDataSource {

    suspend fun saveNowMovieList(movieList: MovieListModel)
    fun getNowMovieListFlow(): Flow<List<MovieModel>>
    suspend fun savePlanMovieList(movieList: MovieListModel)
    fun getPlanMovieListFlow(): Flow<List<MovieModel>>
    suspend fun getNowLastUpdateTime(): Long
    suspend fun getPlanLastUpdateTime(): Long
    suspend fun getAllMovieList(): List<MovieModel>
    suspend fun getNowMovieList(): List<MovieModel>

    fun saveCodeList(response: TheaterAreaGroupModel)
    fun getCodeList(): TheaterAreaGroupModel?

    suspend fun addFavoriteMovie(movie: MovieModel)
    suspend fun removeFavoriteMovie(movieId: String)
    fun getFavoriteMovieList(): Flow<List<MovieModel>>
    suspend fun isFavoriteMovie(movieId: String): Boolean

    suspend fun getOpenDateAlarmListUntil(date: String): List<OpenDateAlarmModel>
    suspend fun hasOpenDateAlarms(): Boolean
    suspend fun insertOpenDateAlarm(alarm: OpenDateAlarmModel)
    suspend fun deleteOpenDateAlarms(alarms: List<OpenDateAlarmModel>)
}
