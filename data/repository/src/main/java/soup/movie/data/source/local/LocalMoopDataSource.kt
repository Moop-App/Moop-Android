package soup.movie.data.source.local

import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import soup.movie.data.mapper.*
import soup.movie.data.model.entity.MovieListEntity
import soup.movie.data.model.entity.MovieListEntity.Companion.TYPE_NOW
import soup.movie.data.model.entity.MovieListEntity.Companion.TYPE_PLAN
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.model.response.TheaterAreaGroupResponse
import soup.movie.model.Movie
import soup.movie.model.MovieDetail
import soup.movie.model.TheaterAreaGroup

class LocalMoopDataSource(
    private val favoriteMovieDao: FavoriteMovieDao,
    private val openDateAlarmDao: OpenDateAlarmDao,
    private val cacheDao: MovieCacheDao
) {

    private var codeResponse: TheaterAreaGroup? = null

    suspend fun saveNowList(response: MovieListResponse) {
        saveMovieListAs(TYPE_NOW, response)
    }

    fun getNowList(): Observable<List<Movie>> {
        return getMovieListAs(TYPE_NOW)
    }

    suspend fun savePlanList(response: MovieListResponse) {
        saveMovieListAs(TYPE_PLAN, response)
    }

    fun getPlanList(): Observable<List<Movie>> {
        return getMovieListAs(TYPE_PLAN)
    }

    private suspend fun saveMovieListAs(type: String, response: MovieListResponse) {
        cacheDao.insert(
            MovieListEntity(
                type,
                response.lastUpdateTime,
                response.list.map { it.toMovieEntity() }
            )
        )
        openDateAlarmDao.updateOpenDateAlarms(response.list.map { it.toOpenDateAlarmEntity() })
    }

    private fun getMovieListAs(type: String): Observable<List<Movie>> {
        return cacheDao.getMovieListByType(type)
            .onErrorReturn { MovieListEntity.empty(type) }
            .map { it.list.map { movieEntity -> movieEntity.toMovie() } }
            .toObservable()
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
            cacheDao.getMovieListOf(type).list.map { movieEntity -> movieEntity.toMovie() }
        } catch (t: Throwable) {
            emptyList()
        }
    }

    fun saveCodeList(response: TheaterAreaGroupResponse) {
        codeResponse = response.toTheaterAreaGroup()
    }

    fun getCodeList(): TheaterAreaGroup? {
        return codeResponse
    }

    suspend fun addFavoriteMovie(movie: MovieDetail) {
        favoriteMovieDao.insertFavoriteMovie(movie.toFavoriteMovie())
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
