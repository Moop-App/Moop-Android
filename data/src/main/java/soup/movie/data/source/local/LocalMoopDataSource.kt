package soup.movie.data.source.local

import io.reactivex.Observable
import soup.movie.data.model.response.CachedMovieList
import soup.movie.data.model.response.CachedMovieList.Companion.TYPE_NOW
import soup.movie.data.model.response.CachedMovieList.Companion.TYPE_PLAN
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.source.MoopDataSource
import soup.movie.data.util.orEmpty
import soup.movie.data.util.toObservable

class LocalMoopDataSource(
    private val moopDao: MoopDao
) : MoopDataSource {

    private var codeResponse: CodeResponse? = null

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
        moopDao.insert(CachedMovieList(type, response.lastUpdateTime, response.list))
    }

    private fun getMovieListAs(type: String): Observable<MovieListResponse> {
        return moopDao.findByType(type)
            .onErrorReturn { CachedMovieList.empty(type) }
            .map { MovieListResponse(it.lastUpdateTime, it.list) }
            .toObservable()
    }

    fun saveCodeList(response: CodeResponse) {
        codeResponse = response
    }

    override fun getCodeList(): Observable<CodeResponse> {
        return codeResponse?.toObservable().orEmpty()
    }
}
