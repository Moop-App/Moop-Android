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
package soup.movie.data.repository

import kotlinx.coroutines.flow.Flow
import soup.movie.model.MovieDetailModel
import soup.movie.model.MovieModel
import soup.movie.model.OpenDateAlarmModel

interface MovieRepository {

    fun getNowMovieList(): Flow<List<MovieModel>>
    suspend fun updateNowMovieList()
    suspend fun updateAndGetNowMovieList(): List<MovieModel>
    fun getPlanMovieList(): Flow<List<MovieModel>>
    suspend fun updatePlanMovieList()
    suspend fun getMovieDetail(movieId: String): MovieDetailModel
    suspend fun getGenreList(): List<String>
    suspend fun searchMovie(query: String): List<MovieModel>

    fun getFavoriteMovieList(): Flow<List<MovieModel>>
    suspend fun addFavoriteMovie(movie: MovieModel)
    suspend fun removeFavoriteMovie(movieId: String)
    suspend fun isFavoriteMovie(movieId: String): Boolean

    suspend fun getOpenDateAlarmListUntil(date: String): List<OpenDateAlarmModel>
    suspend fun hasOpenDateAlarms(): Boolean
    suspend fun insertOpenDateAlarms(alarm: OpenDateAlarmModel)
    suspend fun deleteOpenDateAlarms(alarms: List<OpenDateAlarmModel>)
}
