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
package soup.movie.data.repository.internal

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import soup.movie.data.database.LocalDataSource
import soup.movie.data.network.RemoteDataSource
import soup.movie.data.network.response.asModel
import soup.movie.data.repository.MovieRepository
import soup.movie.data.repository.internal.util.SearchHelper
import soup.movie.model.Movie
import soup.movie.model.MovieDetail
import soup.movie.model.OpenDateAlarm
import soup.movie.model.TheaterAreaGroup
import timber.log.Timber

internal class MovieRepositoryImpl(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher,
) : MovieRepository {

    override fun getNowMovieList(): Flow<List<Movie>> {
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
            Timber.w(t)
            true
        }
        if (isStaleness) {
            local.saveNowMovieList(remote.getNowMovieList().asModel())
        }
    }

    override suspend fun updateAndGetNowMovieList(): List<Movie> {
        return withContext(ioDispatcher) {
            updateNowMovieListInternal()
            local.getNowMovieList()
        }
    }

    override fun getPlanMovieList(): Flow<List<Movie>> {
        return local.getPlanMovieListFlow()
    }

    override suspend fun updatePlanMovieList() {
        return withContext(ioDispatcher) {
            val isStaleness = try {
                local.getPlanLastUpdateTime() < remote.getPlanLastUpdateTime()
            } catch (t: Throwable) {
                Timber.w(t)
                true
            }
            if (isStaleness) {
                local.savePlanMovieList(remote.getPlanMovieList().asModel())
            }
        }
    }

    override suspend fun getMovieDetail(movieId: String): MovieDetail {
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
            Timber.w(t)
            emptyList()
        }
    }

    override suspend fun searchMovie(query: String): List<Movie> {
        return local.getAllMovieList().asSequence()
            .filter { it.isMatchedWith(query) }
            .toList()
    }

    private fun Movie.isMatchedWith(query: String): Boolean {
        return SearchHelper.matched(title, query)
    }

    override suspend fun getCodeList(): TheaterAreaGroup {
        return withContext(ioDispatcher) {
            local.getCodeList() ?: remote.getCodeList()
                .asModel()
                .also(local::saveCodeList)
        }
    }

    override fun getFavoriteMovieList(): Flow<List<Movie>> {
        return local.getFavoriteMovieList()
    }

    override suspend fun addFavoriteMovie(movie: Movie) {
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
    override suspend fun getOpenDateAlarmListUntil(date: String): List<OpenDateAlarm> {
        return local.getOpenDateAlarmListUntil(date)
    }

    override suspend fun hasOpenDateAlarms(): Boolean {
        return local.hasOpenDateAlarms()
    }

    override suspend fun insertOpenDateAlarms(alarm: OpenDateAlarm) {
        local.insertOpenDateAlarm(alarm)
    }

    override suspend fun deleteOpenDateAlarms(alarms: List<OpenDateAlarm>) {
        return local.deleteOpenDateAlarms(alarms)
    }
}
