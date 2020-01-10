package soup.movie.data.source.local

import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import soup.movie.data.model.Movie
import soup.movie.data.model.MovieDetail
import soup.movie.data.model.MovieTheater
import soup.movie.data.model.entity.CachedMovieList
import soup.movie.data.model.entity.CachedMovieList.Companion.TYPE_NOW
import soup.movie.data.model.entity.CachedMovieList.Companion.TYPE_PLAN
import soup.movie.data.model.entity.FavoriteMovie
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.source.MoopDataSource

class LocalMoopDataSource(
    private val moopDao: MoopDao
) : MoopDataSource {

    private var codeResponse: CodeResponse? = null

    fun saveNowList(response: MovieListResponse) {
        saveMovieListAs(TYPE_NOW, response)
    }

    fun getNowList(): Observable<MovieListResponse> {
        return getMovieListAs(TYPE_NOW)
    }

    fun savePlanList(response: MovieListResponse) {
        saveMovieListAs(TYPE_PLAN, response)
    }

    fun getPlanList(): Observable<MovieListResponse> {
        return getMovieListAs(TYPE_PLAN)
    }

    private fun saveMovieListAs(type: String, response: MovieListResponse) {
        moopDao.insert(
            CachedMovieList(
                type,
                response.lastUpdateTime,
                response.list
            )
        )
    }

    private fun getMovieListAs(type: String): Observable<MovieListResponse> {
        return moopDao.getMovieListByType(type)
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
        return moopDao.findByType(type).run {
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
            moopDao.getMovieListOf(type).list
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
        moopDao.addFavoriteMovie(movie.toFavoriteMovie())
    }
    suspend fun removeFavoriteMovie(movieId: String) {
        moopDao.removeFavoriteMovie(movieId)
    }
    fun getFavoriteMovieList(): Flow<List<Movie>> {
        return moopDao.getFavoriteMovieList().map { it.map {
            Movie(
                id = it.id,
                score = 0,
                title = it.title,
                _posterUrl = it.posterUrl,
                openDate = it.openDate,
                isNow = it.isNow,
                age = it.age,
                nationFilter = it.nationFilter,
                genres = it.genres,
                boxOffice = it.boxOffice,
                theater = MovieTheater(
                    cgv = it.cgv,
                    lotte = it.lotte,
                    megabox = it.megabox
                )
            )
        } }
    }
    suspend fun isFavoriteMovie(movieId: String): Boolean {
        return moopDao.getCountForFavoriteMovie(movieId) > 0
    }

    private fun MovieDetail.toFavoriteMovie(): FavoriteMovie {
        return FavoriteMovie(
            id = id,
            title = title,
            posterUrl = posterUrl,
            openDate = openDate,
            isNow = isNow,
            age = age,
            nationFilter = nationFilter,
            genres = genres,
            boxOffice = boxOffice?.rank,
            cgv = cgv?.star,
            lotte = lotte?.star,
            megabox = megabox?.star
        )
    }
}
