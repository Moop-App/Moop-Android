package soup.movie.data.source.local

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import soup.movie.data.model.request.TimeTableRequest
import soup.movie.data.model.response.CachedMovieList
import soup.movie.data.model.response.CachedMovieList.Companion.TYPE_NOW
import soup.movie.data.model.response.CachedMovieList.Companion.TYPE_PLAN
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.model.response.TimeTableResponse
import soup.movie.data.source.MoobDataSource

class LocalMoobDataSource(private val moobDao: MoobDao) : MoobDataSource {

    fun saveNowList(response: MovieListResponse) {
        moobDao.insert(CachedMovieList(TYPE_NOW, System.currentTimeMillis(), response.list))
    }

    override fun getNowList(): Observable<MovieListResponse> {
        return moobDao.findByType(TYPE_NOW)
                .onErrorReturn { CachedMovieList.empty(TYPE_NOW) }
                .toObservable()
                .filter { it.isUpToDate() }
                .map { MovieListResponse(it.lastUpdateTime, it.list) }
                .subscribeOn(Schedulers.io())
    }

    fun savePlanList(response: MovieListResponse) {
        moobDao.insert(CachedMovieList(TYPE_PLAN, System.currentTimeMillis(), response.list))
    }

    override fun getPlanList(): Observable<MovieListResponse> {
        return moobDao.findByType(TYPE_PLAN)
                .onErrorReturn { CachedMovieList.empty(TYPE_PLAN) }
                .toObservable()
                .filter { it.isUpToDate() }
                .map { MovieListResponse(it.lastUpdateTime, it.list) }
                .subscribeOn(Schedulers.io())
    }

    override fun getCodeList(): Observable<CodeResponse> = TODO()

    override fun getTimeTableList(request: TimeTableRequest): Observable<TimeTableResponse> = TODO()
}
