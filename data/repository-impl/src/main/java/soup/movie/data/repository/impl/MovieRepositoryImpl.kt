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
package soup.movie.data.repository.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import soup.movie.common.IoDispatcher
import soup.movie.data.database.LocalDataSource
import soup.movie.data.network.RemoteDataSource
import soup.movie.data.network.response.asModel
import soup.movie.data.repository.MovieRepository
import soup.movie.data.repository.impl.util.SearchHelper
import soup.movie.log.Logger
import soup.movie.model.MovieDetailModel
import soup.movie.model.MovieModel
import soup.movie.model.OpenDateAlarmModel
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : MovieRepository {

    override fun getNowMovieList(): Flow<List<MovieModel>> {
        return local.getNowMovieListFlow()
    }

    override suspend fun updateNowMovieList() {
        return withContext(ioDispatcher) {
            updateNowMovieListInternal()
        }
    }

    private suspend fun updateNowMovieListInternal() {
        val isStaleness = try {
            local.getNowLastUpdateTime() < remote.getNowLastUpdateTime()
        } catch (t: Throwable) {
            Logger.w(t)
            true
        }
        if (isStaleness) {
            local.saveNowMovieList(remote.getNowMovieList().asModel())
        }
    }

    override suspend fun updateAndGetNowMovieList(): List<MovieModel> {
        return withContext(ioDispatcher) {
            updateNowMovieListInternal()
            local.getNowMovieList()
        }
    }

    override fun getPlanMovieList(): Flow<List<MovieModel>> {
        return local.getPlanMovieListFlow()
    }

    override suspend fun updatePlanMovieList() {
        return withContext(ioDispatcher) {
            val isStaleness = try {
                local.getPlanLastUpdateTime() < remote.getPlanLastUpdateTime()
            } catch (t: Throwable) {
                Logger.w(t)
                true
            }
            if (isStaleness) {
                local.savePlanMovieList(remote.getPlanMovieList().asModel())
            }
        }
    }

    override suspend fun getMovieDetail(movieId: String): MovieDetailModel {
        return withContext(ioDispatcher) {
            remote.getMovieDetail(movieId).asModel()
        }
    }

    override suspend fun getGenreList(): List<String> {
        return try {
            local.getAllMovieList()
                .mapNotNull { it.genres }
                .flatten()
                .toSet()
                .toList()
        } catch (t: Throwable) {
            Logger.w(t)
            emptyList()
        }
    }

    override suspend fun searchMovie(query: String): List<MovieModel> {
        return local.getAllMovieList().asSequence()
            .filter { it.isMatchedWith(query) }
            .toList()
    }

    private fun MovieModel.isMatchedWith(query: String): Boolean {
        return SearchHelper.matched(title, query)
    }

    override fun getFavoriteMovieList(): Flow<List<MovieModel>> {
        return local.getFavoriteMovieList()
    }

    override suspend fun addFavoriteMovie(movie: MovieModel) {
        local.addFavoriteMovie(movie)
    }

    override suspend fun removeFavoriteMovie(movieId: String) {
        local.removeFavoriteMovie(movieId)
    }

    override suspend fun isFavoriteMovie(movieId: String): Boolean {
        return local.isFavoriteMovie(movieId)
    }

    /**
     * @param date yyyy.mm.dd ex) 2020.01.31
     */
    override suspend fun getOpenDateAlarmListUntil(date: String): List<OpenDateAlarmModel> {
        return local.getOpenDateAlarmListUntil(date)
    }

    override suspend fun hasOpenDateAlarms(): Boolean {
        return local.hasOpenDateAlarms()
    }

    override suspend fun insertOpenDateAlarms(alarm: OpenDateAlarmModel) {
        local.insertOpenDateAlarm(alarm)
    }

    override suspend fun deleteOpenDateAlarms(alarms: List<OpenDateAlarmModel>) {
        return local.deleteOpenDateAlarms(alarms)
    }
}
