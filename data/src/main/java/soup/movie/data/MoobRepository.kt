package soup.movie.data

import io.reactivex.Observable
import soup.movie.data.model.Movie
import soup.movie.data.model.Version
import soup.movie.data.model.request.TimetableRequest
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.model.response.TimetableResponse
import soup.movie.data.source.local.LocalMoobDataSource
import soup.movie.data.source.remote.RemoteMoobDataSource

class MoobRepository(private val localDataSource: LocalMoobDataSource,
                     private val remoteDataSource: RemoteMoobDataSource) {

    private var codeResponse: CodeResponse? = null
    private var version: Version? = null

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

    fun getMovie(movieId: String): Observable<Movie> =
            Observable.merge(
                    getNowList(false).map { it.list },
                    getPlanList(false).map { it.list })
                    .flatMapIterable { it -> it }
                    .filter { it.id == movieId }

    fun getCodeList(): Observable<CodeResponse> {
        return codeResponse
                ?.let { Observable.just(it) }
                ?: run { remoteDataSource.getCodeList()
                        .doOnNext { codeResponse = it }
                }
    }

    fun getTimetable(request: TimetableRequest): Observable<TimetableResponse> {
        return remoteDataSource.getTimetable(request)
    }

    fun getVersion(pkgName: String, defaultVersion: String): Observable<Version> {
        return version
                ?.let { Observable.just(it) }
                ?: run { remoteDataSource.getVersion(pkgName, defaultVersion)
                        .startWith(Version(defaultVersion))
                        .doOnNext { version = it }
                }
    }
}
