package soup.movie.data.repository

import kotlinx.coroutines.flow.Flow
import soup.movie.data.api.MoopApiService
import soup.movie.data.db.LocalMoopDataSource
import soup.movie.data.repository.mapper.toMovieDetail
import soup.movie.data.repository.mapper.toMovieList
import soup.movie.data.repository.mapper.toTheaterAreaGroup
import soup.movie.data.repository.util.SearchHelper
import soup.movie.model.Movie
import soup.movie.model.MovieDetail
import soup.movie.model.OpenDateAlarm
import soup.movie.model.TheaterAreaGroup

class MoopRepository(
    private val local: LocalMoopDataSource,
    private val remote: MoopApiService
) {

    fun getNowMovieList(): Flow<List<Movie>> {
        return local.getNowMovieListFlow()
    }

    suspend fun updateNowMovieList() {
        val isStaleness = try {
            local.getNowLastUpdateTime() < remote.getNowLastUpdateTime()
        } catch (t: Throwable) {
            true
        }
        if (isStaleness) {
            local.saveNowMovieList(remote.getNowMovieList().toMovieList())
        }
    }

    suspend fun updateAndGetNowMovieList(): List<Movie> {
        updateNowMovieList()
        return local.getNowMovieList()
    }

    fun getPlanMovieList(): Flow<List<Movie>> {
        return local.getPlanMovieListFlow()
    }

    suspend fun updatePlanMovieList() {
        val isStaleness = try {
            local.getPlanLastUpdateTime() < remote.getPlanLastUpdateTime()
        } catch (t: Throwable) {
            true
        }
        if (isStaleness) {
            local.savePlanMovieList(remote.getPlanMovieList().toMovieList())
        }
    }

    suspend fun getMovieDetail(movieId: String): MovieDetail {
        return remote.getMovieDetail(movieId).toMovieDetail()
    }

    suspend fun getGenreList(): List<String> {
        return try {
            local.getAllMovieList()
                .mapNotNull { it.genres }
                .flatten()
                .toSet()
                .toList()
        } catch (t: Throwable) {
            emptyList()
        }
    }

    suspend fun findMovie(movieId: String): Movie? {
        return local.getAllMovieList()
            .find { it.id == movieId }
    }

    suspend fun searchMovie(query: String): List<Movie> {
        return local.getAllMovieList().asSequence()
            .filter { it.isMatchedWith(query) }
            .toList()
    }

    private fun Movie.isMatchedWith(query: String): Boolean {
        return SearchHelper.matched(title, query)
    }

    suspend fun getCodeList(): TheaterAreaGroup {
        return local.getCodeList()
            ?: remote.getCodeList()
                .toTheaterAreaGroup()
                .also(local::saveCodeList)
    }

    fun getFavoriteMovieList(): Flow<List<Movie>> {
        return local.getFavoriteMovieList()
    }

    suspend fun addFavoriteMovie(movie: Movie) {
        local.addFavoriteMovie(movie)
    }

    suspend fun removeFavoriteMovie(movieId: String) {
        local.removeFavoriteMovie(movieId)
    }

    suspend fun isFavoriteMovie(movieId: String): Boolean {
        return local.isFavoriteMovie(movieId)
    }

    /**
     * @param date yyyy.mm.dd ex) 2020.01.31
     */
    suspend fun getOpenDateAlarmListUntil(date: String): List<OpenDateAlarm> {
        return local.getOpenDateAlarmListUntil(date)
    }

    suspend fun hasOpenDateAlarms(): Boolean {
        return local.hasOpenDateAlarms()
    }

    suspend fun deleteOpenDateAlarms(alarms: List<OpenDateAlarm>) {
        return local.deleteOpenDateAlarms(alarms)
    }
}
