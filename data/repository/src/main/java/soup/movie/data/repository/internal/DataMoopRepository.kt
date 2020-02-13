package soup.movie.data.repository.internal

import kotlinx.coroutines.flow.Flow
import soup.movie.data.api.MoopApiService
import soup.movie.data.db.MoopDatabase
import soup.movie.data.repository.internal.mapper.toMovieDetail
import soup.movie.data.repository.internal.mapper.toMovieList
import soup.movie.data.repository.internal.mapper.toTheaterAreaGroup
import soup.movie.data.repository.internal.util.SearchHelper
import soup.movie.model.Movie
import soup.movie.model.MovieDetail
import soup.movie.model.OpenDateAlarm
import soup.movie.model.TheaterAreaGroup
import soup.movie.model.repository.MoopRepository

internal class DataMoopRepository(
    private val local: MoopDatabase,
    private val remote: MoopApiService
): MoopRepository {

    override fun getNowMovieList(): Flow<List<Movie>> {
        return local.getNowMovieListFlow()
    }

    override suspend fun updateNowMovieList() {
        val isStaleness = try {
            local.getNowLastUpdateTime() < remote.getNowLastUpdateTime()
        } catch (t: Throwable) {
            true
        }
        if (isStaleness) {
            local.saveNowMovieList(remote.getNowMovieList().toMovieList())
        }
    }

    override suspend fun updateAndGetNowMovieList(): List<Movie> {
        updateNowMovieList()
        return local.getNowMovieList()
    }

    override fun getPlanMovieList(): Flow<List<Movie>> {
        return local.getPlanMovieListFlow()
    }

    override suspend fun updatePlanMovieList() {
        val isStaleness = try {
            local.getPlanLastUpdateTime() < remote.getPlanLastUpdateTime()
        } catch (t: Throwable) {
            true
        }
        if (isStaleness) {
            local.savePlanMovieList(remote.getPlanMovieList().toMovieList())
        }
    }

    override suspend fun getMovieDetail(movieId: String): MovieDetail {
        return remote.getMovieDetail(movieId).toMovieDetail()
    }

    override suspend fun getGenreList(): List<String> {
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

    override suspend fun findMovie(movieId: String): Movie? {
        return local.getAllMovieList()
            .find { it.id == movieId }
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
        return local.getCodeList()
            ?: remote.getCodeList()
                .toTheaterAreaGroup()
                .also(local::saveCodeList)
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
