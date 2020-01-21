package soup.movie.data.source.local

import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import soup.movie.data.model.Movie
import soup.movie.data.model.MovieDetail
import soup.movie.data.model.converter.EntityConverter
import soup.movie.data.model.entity.CachedMovieList
import soup.movie.data.model.entity.CachedMovieList.Companion.TYPE_NOW
import soup.movie.data.model.entity.CachedMovieList.Companion.TYPE_PLAN
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.source.MoopDataSource

class LocalMoopDataSource(
    private val favoriteMovieDao: FavoriteMovieDao,
    private val openDateAlarmDao: OpenDateAlarmDao,
    private val cacheDao: MovieCacheDao
) : MoopDataSource, EntityConverter {

    private var codeResponse: CodeResponse? = null

    suspend fun saveNowList(response: MovieListResponse) {
        saveMovieListAs(TYPE_NOW, response)
    }

    fun getNowList(): Observable<MovieListResponse> {
        return getMovieListAs(TYPE_NOW)
    }

    suspend fun savePlanList(response: MovieListResponse) {
        saveMovieListAs(TYPE_PLAN, response)
    }

    fun getPlanList(): Observable<MovieListResponse> {
        return getMovieListAs(TYPE_PLAN)
    }

    private suspend fun saveMovieListAs(type: String, response: MovieListResponse) {
        cacheDao.insert(
            CachedMovieList(
                type,
                response.lastUpdateTime,
                response.list
            )
        )
        openDateAlarmDao.updateOpenDateAlarms(response.list.map { it.toOpenDateAlarm() })
    }

    private fun getMovieListAs(type: String): Observable<MovieListResponse> {
        return cacheDao.getMovieListByType(type)
            .onErrorReturn { CachedMovieList.empty(type) }
            .map { MovieListResponse(it.lastUpdateTime, it.list) }
            .toObservable()
    }

    suspend fun findNowMovieList() : MovieListResponse {
        return findMovieListAs(TYPE_NOW)
    }

    suspend fun findPlanMovieList() : MovieListResponse {
        return findMovieListAs(TYPE_PLAN)
    }

    private suspend fun findMovieListAs(type: String): MovieListResponse {
        return cacheDao.findByType(type).run {
            MovieListResponse(lastUpdateTime, list)
        }
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
            cacheDao.getMovieListOf(type).list
        } catch (t: Throwable) {
            emptyList()
        }
    }

    fun saveCodeList(response: CodeResponse) {
        codeResponse = response
    }

    fun getCodeList(): CodeResponse? {
        return codeResponse
    }

    suspend fun addFavoriteMovie(movie: MovieDetail) {
        favoriteMovieDao.insertFavoriteMovie(movie.toFavoriteMovie())
        if (movie.isPlan) {
            openDateAlarmDao.insertOpenDateAlarm(movie.toOpenDateAlarm())
        }
    }

    suspend fun removeFavoriteMovie(movieId: String) {
        favoriteMovieDao.deleteFavoriteMovie(movieId)
        openDateAlarmDao.deleteOpenDateAlarm(movieId)
    }

    fun getFavoriteMovieList(): Flow<List<Movie>> {
        return favoriteMovieDao.getFavoriteMovieList().map {
            it.map { it.toMovie() }
        }
    }

    suspend fun isFavoriteMovie(movieId: String): Boolean {
        return favoriteMovieDao.getCountForFavoriteMovie(movieId) > 0
    }
}
