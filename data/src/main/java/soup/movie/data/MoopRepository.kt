package soup.movie.data

import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import soup.movie.data.model.Movie
import soup.movie.data.model.MovieDetail
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.source.local.LocalMoopDataSource
import soup.movie.data.source.remote.RemoteMoopDataSource
import soup.movie.data.util.SearchHelper

class MoopRepository(
    private val local: LocalMoopDataSource,
    private val remote: RemoteMoopDataSource
) {

    fun getNowList(): Observable<MovieListResponse> {
        return local.getNowList()
    }

    suspend fun updateNowList() {
        val isStaleness = try {
            local.findNowMovieList().lastUpdateTime < remote.getNowLastUpdateTime()
        } catch (t: Throwable) {
            true
        }
        if (isStaleness) {
            local.saveNowList(remote.getNowList())
        }
    }

    fun getPlanList(): Observable<MovieListResponse> {
        return local.getPlanList()
    }

    suspend fun updatePlanList() {
        val isStaleness = try {
            local.findPlanMovieList().lastUpdateTime < remote.getPlanLastUpdateTime()
        } catch (t: Throwable) {
            true
        }
        if (isStaleness) {
            local.savePlanList(remote.getPlanList())
        }
    }

    suspend fun getMovieDetail(movieId: String): MovieDetail {
        return remote.getMovieDetail(movieId)
    }

    fun getMovie(movieId: String): Observable<Movie> =
        Observable.merge(
            getNowList().map { it.list },
            getPlanList().map { it.list })
            .flatMapIterable { it }
            .filter { it.id == movieId }
            .take(1)

    suspend fun searchMovie(query: String): List<Movie> {
        return local.getAllMovieList().asSequence()
            .filter { it.isMatchedWith(query) }
            .toList()
    }

    private fun Movie.isMatchedWith(query: String): Boolean {
        return SearchHelper.matched(title, query)
    }

    suspend fun getCodeList(): CodeResponse {
        return local.getCodeList()
            ?: remote.getCodeList()
                .also(local::saveCodeList)
    }

    //TODO: manage using DB
    fun getFavoriteMovieList(): Flow<List<Movie>> {
        return local.getFavoriteMovieList()
    }
    suspend fun addFavoriteMovie(movie: MovieDetail) {
        local.addFavoriteMovie(movie)
    }
    suspend fun removeFavoriteMovie(movieId: String) {
        local.removeFavoriteMovie(movieId)
    }
    suspend fun isFavoriteMovie(movieId: String): Boolean {
        return local.isFavoriteMovie(movieId)
    }
}
