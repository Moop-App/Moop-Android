package soup.movie.data.db

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import soup.movie.data.db.entity.MovieListEntity
import soup.movie.data.db.entity.MovieListEntity.Companion.TYPE_NOW
import soup.movie.data.db.entity.MovieListEntity.Companion.TYPE_PLAN
import soup.movie.data.db.internal.dao.FavoriteMovieDao
import soup.movie.data.db.internal.dao.MovieCacheDao
import soup.movie.data.db.internal.dao.OpenDateAlarmDao
import soup.movie.data.db.mapper.toFavoriteMovieEntity
import soup.movie.data.db.mapper.toMovie
import soup.movie.data.db.mapper.toMovieEntity
import soup.movie.data.db.mapper.toOpenDateAlarmEntity
import soup.movie.model.Movie
import soup.movie.model.MovieDetail
import soup.movie.model.MovieList
import soup.movie.model.TheaterAreaGroup

class LocalMoopDataSource(
    private val favoriteMovieDao: FavoriteMovieDao,
    private val openDateAlarmDao: OpenDateAlarmDao,
    private val cacheDao: MovieCacheDao
) {

    private var codeResponse: TheaterAreaGroup? = null

    suspend fun saveNowList(movieList: MovieList) {
        saveMovieListAs(TYPE_NOW, movieList)
    }

    fun getNowMovieListFlow(): Flow<List<Movie>> {
        return getMovieList(TYPE_NOW)
    }

    suspend fun savePlanList(movieList: MovieList) {
        saveMovieListAs(TYPE_PLAN, movieList)
    }

    fun getPlanMovieListFlow(): Flow<List<Movie>> {
        return getMovieList(TYPE_PLAN)
    }

    private suspend fun saveMovieListAs(type: String, movieList: MovieList) {
        cacheDao.insert(
            MovieListEntity(
                type,
                movieList.lastUpdateTime,
                movieList.list.map { it.toMovieEntity() }
            )
        )
        openDateAlarmDao.updateOpenDateAlarms(movieList.list.map { it.toOpenDateAlarmEntity() })
    }

    private fun getMovieList(type: String): Flow<List<Movie>> {
        return cacheDao.getMovieListByType(type)
            .map { it.list.map { movieEntity -> movieEntity.toMovie() } }
            .catch { emit(emptyList()) }
    }

    suspend fun getNowLastUpdateTime() : Long {
        return cacheDao.findByType(TYPE_NOW).lastUpdateTime
    }

    suspend fun getPlanLastUpdateTime() : Long {
        return cacheDao.findByType(TYPE_PLAN).lastUpdateTime
    }

    suspend fun getAllMovieList(): List<Movie> {
        return getNowMovieList() + getPlanMovieList()
    }

    private suspend fun getNowMovieList(): List<Movie> {
        return getMovieListOf(TYPE_NOW)
    }

    private suspend fun getPlanMovieList(): List<Movie> {
        return getMovieListOf(TYPE_PLAN)
    }

    private suspend fun getMovieListOf(type: String): List<Movie> {
        return try {
            cacheDao.findByType(type).list
                .map { movieEntity -> movieEntity.toMovie() }
        } catch (t: Throwable) {
            emptyList()
        }
    }

    fun saveCodeList(response: TheaterAreaGroup) {
        codeResponse = response
    }

    fun getCodeList(): TheaterAreaGroup? {
        return codeResponse
    }

    suspend fun addFavoriteMovie(movie: MovieDetail) {
        favoriteMovieDao.insertFavoriteMovie(movie.toFavoriteMovieEntity())
        if (movie.isPlan) {
            openDateAlarmDao.insertOpenDateAlarm(movie.toOpenDateAlarmEntity())
        }
    }

    suspend fun removeFavoriteMovie(movieId: String) {
        favoriteMovieDao.deleteFavoriteMovie(movieId)
        openDateAlarmDao.deleteOpenDateAlarm(movieId)
    }

    fun getFavoriteMovieList(): Flow<List<Movie>> {
        return favoriteMovieDao.getFavoriteMovieList().map {
            it.map { favoriteMovieEntity -> favoriteMovieEntity.toMovie() }
        }
    }

    suspend fun isFavoriteMovie(movieId: String): Boolean {
        return favoriteMovieDao.getCountForFavoriteMovie(movieId) > 0
    }
}
