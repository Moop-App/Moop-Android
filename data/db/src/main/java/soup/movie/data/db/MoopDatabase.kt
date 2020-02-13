package soup.movie.data.db

import kotlinx.coroutines.flow.Flow
import soup.movie.model.Movie
import soup.movie.model.MovieList
import soup.movie.model.OpenDateAlarm
import soup.movie.model.TheaterAreaGroup

interface MoopDatabase {

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
