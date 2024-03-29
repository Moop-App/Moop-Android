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
package soup.movie.data.database.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import soup.movie.data.database.LocalDataSource
import soup.movie.data.database.impl.dao.FavoriteMovieDao
import soup.movie.data.database.impl.dao.MovieCacheDao
import soup.movie.data.database.impl.dao.OpenDateAlarmDao
import soup.movie.data.database.impl.entity.MovieListEntity
import soup.movie.data.database.impl.entity.MovieListEntity.Companion.TYPE_NOW
import soup.movie.data.database.impl.entity.MovieListEntity.Companion.TYPE_PLAN
import soup.movie.data.database.impl.mapper.toEntity
import soup.movie.data.database.impl.mapper.toFavoriteMovieEntity
import soup.movie.data.database.impl.mapper.toMovie
import soup.movie.data.database.impl.mapper.toMovieEntity
import soup.movie.data.database.impl.mapper.toOpenDateAlarm
import soup.movie.data.database.impl.mapper.toOpenDateAlarmEntity
import soup.movie.log.Logger
import soup.movie.model.MovieListModel
import soup.movie.model.MovieModel
import soup.movie.model.OpenDateAlarmModel
import soup.movie.model.TheaterAreaGroupModel
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao,
    private val openDateAlarmDao: OpenDateAlarmDao,
    private val cacheDao: MovieCacheDao,
) : LocalDataSource {

    private var codeResponse: TheaterAreaGroupModel? = null

    override suspend fun saveNowMovieList(movieList: MovieListModel) {
        saveMovieListAs(TYPE_NOW, movieList)
        favoriteMovieDao.updateAll(movieList.list.map { it.toFavoriteMovieEntity() })
    }

    override fun getNowMovieListFlow(): Flow<List<MovieModel>> {
        return getMovieListFlow(TYPE_NOW)
    }

    override suspend fun savePlanMovieList(movieList: MovieListModel) {
        saveMovieListAs(TYPE_PLAN, movieList)
        favoriteMovieDao.updateAll(movieList.list.map { it.toFavoriteMovieEntity() })
        openDateAlarmDao.updateAll(movieList.list.map { it.toOpenDateAlarmEntity() })
    }

    override fun getPlanMovieListFlow(): Flow<List<MovieModel>> {
        return getMovieListFlow(TYPE_PLAN)
    }

    private suspend fun saveMovieListAs(type: String, movieList: MovieListModel) {
        cacheDao.insert(
            MovieListEntity(
                type,
                movieList.lastUpdateTime,
                movieList.list.map { it.toMovieEntity() },
            ),
        )
    }

    private fun getMovieListFlow(type: String): Flow<List<MovieModel>> {
        return cacheDao.getMovieListByType(type)
            .map { it.list.map { movieEntity -> movieEntity.toMovie() } }
            .catch { emit(emptyList()) }
    }

    override suspend fun getNowLastUpdateTime(): Long {
        return cacheDao.findByType(TYPE_NOW).lastUpdateTime
    }

    override suspend fun getPlanLastUpdateTime(): Long {
        return cacheDao.findByType(TYPE_PLAN).lastUpdateTime
    }

    override suspend fun getAllMovieList(): List<MovieModel> {
        return getNowMovieList() + getPlanMovieList()
    }

    override suspend fun getNowMovieList(): List<MovieModel> {
        return getMovieListOf(TYPE_NOW)
    }

    private suspend fun getPlanMovieList(): List<MovieModel> {
        return getMovieListOf(TYPE_PLAN)
    }

    private suspend fun getMovieListOf(type: String): List<MovieModel> {
        return try {
            cacheDao.findByType(type).list
                .map { movieEntity -> movieEntity.toMovie() }
        } catch (t: Throwable) {
            Logger.w(t)
            emptyList()
        }
    }

    override fun saveCodeList(response: TheaterAreaGroupModel) {
        codeResponse = response
    }

    override fun getCodeList(): TheaterAreaGroupModel? {
        return codeResponse
    }

    override suspend fun addFavoriteMovie(movie: MovieModel) {
        favoriteMovieDao.insertFavoriteMovie(movie.toFavoriteMovieEntity())
    }

    override suspend fun removeFavoriteMovie(movieId: String) {
        favoriteMovieDao.deleteFavoriteMovie(movieId)
        openDateAlarmDao.delete(movieId)
    }

    override fun getFavoriteMovieList(): Flow<List<MovieModel>> {
        return favoriteMovieDao.getFavoriteMovieList().map {
            it.map { favoriteMovieEntity -> favoriteMovieEntity.toMovie() }
        }
    }

    override suspend fun isFavoriteMovie(movieId: String): Boolean {
        return favoriteMovieDao.isFavoriteMovie(movieId)
    }

    /**
     * @param date yyyy.mm.dd ex) 2020.01.31
     */
    override suspend fun getOpenDateAlarmListUntil(date: String): List<OpenDateAlarmModel> {
        return openDateAlarmDao.getAllUntil(date)
            .map { openDateAlarmEntity -> openDateAlarmEntity.toOpenDateAlarm() }
    }

    override suspend fun hasOpenDateAlarms(): Boolean {
        return openDateAlarmDao.hasAlarms()
    }

    override suspend fun insertOpenDateAlarm(alarm: OpenDateAlarmModel) {
        openDateAlarmDao.insert(alarm.toEntity())
    }

    override suspend fun deleteOpenDateAlarms(alarms: List<OpenDateAlarmModel>) {
        return openDateAlarmDao.deleteAll(alarms.map { it.movieId })
    }
}
