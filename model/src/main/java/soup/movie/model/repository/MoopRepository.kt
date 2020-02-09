package soup.movie.model.repository

import kotlinx.coroutines.flow.Flow
import soup.movie.model.Movie
import soup.movie.model.MovieDetail
import soup.movie.model.OpenDateAlarm
import soup.movie.model.TheaterAreaGroup

interface MoopRepository {

    fun getNowMovieList(): Flow<List<Movie>>
    suspend fun updateNowMovieList()
    suspend fun updateAndGetNowMovieList(): List<Movie>
    fun getPlanMovieList(): Flow<List<Movie>>
    suspend fun updatePlanMovieList()
    suspend fun getMovieDetail(movieId: String): MovieDetail
    suspend fun getGenreList(): List<String>
    suspend fun findMovie(movieId: String): Movie?
    suspend fun searchMovie(query: String): List<Movie>
    suspend fun getCodeList(): TheaterAreaGroup
    fun getFavoriteMovieList(): Flow<List<Movie>>
    suspend fun addFavoriteMovie(movie: Movie)
    suspend fun removeFavoriteMovie(movieId: String)
    suspend fun isFavoriteMovie(movieId: String): Boolean
    suspend fun getOpenDateAlarmListUntil(date: String): List<OpenDateAlarm>
    suspend fun hasOpenDateAlarms(): Boolean
    suspend fun deleteOpenDateAlarms(alarms: List<OpenDateAlarm>)
}
