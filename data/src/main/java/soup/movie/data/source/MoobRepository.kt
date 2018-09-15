package soup.movie.data.source

import io.reactivex.Observable
import soup.movie.data.model.request.TimeTableRequest
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.model.response.TimeTableResponse
import soup.movie.data.source.local.LocalMoobDataSource
import soup.movie.data.source.remote.RemoteMoobDataSource

class MoobRepository(private val localDataSource: LocalMoobDataSource,
                     private val remoteDataSource: RemoteMoobDataSource) {

    private var codeResponse: CodeResponse? = null

    fun getNowList(clearCache: Boolean): Observable<MovieListResponse> = when {
        clearCache -> getNowListFromNetwork()
        else -> Observable.concat(
                getNowListFromDB(),
                getNowListFromNetwork())
                .take(1)
    }

    private fun getNowListFromDB(): Observable<MovieListResponse> =
            localDataSource.getNowList()

    private fun getNowListFromNetwork() : Observable<MovieListResponse> =
            remoteDataSource.getNowList()
                    .doOnNext { localDataSource.saveNowList(it) }

    fun getPlanList(clearCache: Boolean): Observable<MovieListResponse> = when {
        clearCache -> getPlanListFromNetwork()
        else -> Observable.concat(
                getPlanListFromDB(),
                getPlanListFromNetwork())
                .take(1)
    }

    private fun getPlanListFromDB(): Observable<MovieListResponse> =
            localDataSource.getPlanList()

    private fun getPlanListFromNetwork() : Observable<MovieListResponse> =
            remoteDataSource.getPlanList()
                    .doOnNext { localDataSource.savePlanList(it) }

    fun getCodeList(): Observable<CodeResponse> {
        return codeResponse
                ?.let { Observable.just(it) }
                ?: run { remoteDataSource.getCodeList()
                        .doOnNext { codeResponse = it }
                }
    }

    fun getTimeTableList(request: TimeTableRequest): Observable<TimeTableResponse> {
        return remoteDataSource.getTimeTableList(request)
    }
}
