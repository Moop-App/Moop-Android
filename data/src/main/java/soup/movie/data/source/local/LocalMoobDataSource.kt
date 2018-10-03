package soup.movie.data.source.local

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import soup.movie.data.model.Movie
import soup.movie.data.model.Theater
import soup.movie.data.model.Timetable
import soup.movie.data.model.Version
import soup.movie.data.model.response.CachedMovieList
import soup.movie.data.model.response.CachedMovieList.Companion.TYPE_NOW
import soup.movie.data.model.response.CachedMovieList.Companion.TYPE_PLAN
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.source.MoobDataSource

class LocalMoobDataSource(private val moobDao: MoobDao) : MoobDataSource {

    fun saveNowList(response: MovieListResponse) {
        saveMovieListAs(TYPE_NOW, response)
    }

    override fun getNowList(): Observable<MovieListResponse> {
        return getMovieListAs(TYPE_NOW)
    }

    fun savePlanList(response: MovieListResponse) {
        saveMovieListAs(TYPE_PLAN, response)
    }

    override fun getPlanList(): Observable<MovieListResponse> {
        return getMovieListAs(TYPE_PLAN)
    }

    private fun saveMovieListAs(type: String, response: MovieListResponse) {
        moobDao.insert(CachedMovieList(type, System.currentTimeMillis(), response.list))
    }

    private fun getMovieListAs(type: String): Observable<MovieListResponse> {
        return moobDao.findByType(type)
                .onErrorReturn { CachedMovieList.empty(type) }
                .toObservable()
                .filter { it.isUpToDate() }
                .map { MovieListResponse(it.lastUpdateTime, it.list) }
                .subscribeOn(Schedulers.io())
    }

    override fun getCodeList(): Observable<CodeResponse> = TODO()

    override fun getTimetable(theater: Theater, movie: Movie): Observable<Timetable> = TODO()

    override fun getVersion(pkgName: String, defaultVersion: String): Observable<Version> = TODO()
}
