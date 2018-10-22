package soup.movie.data

import io.reactivex.Observable
import soup.movie.data.model.Movie
import soup.movie.data.model.Theater
import soup.movie.data.model.Timetable
import soup.movie.data.model.Version
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.source.local.LocalMoopDataSource
import soup.movie.data.source.remote.RemoteMoopDataSource

class MoopRepository(private val localDataSource: LocalMoopDataSource,
                     private val remoteDataSource: RemoteMoopDataSource) {

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

    fun getCodeList(): Observable<CodeResponse> =
            Observable.concat(
                    getCodeListInMemory(),
                    getCodeListFromNetwork())
                    .take(1)

    private fun getCodeListInMemory(): Observable<CodeResponse> =
            localDataSource.getCodeList()

    private fun getCodeListFromNetwork() : Observable<CodeResponse> =
            remoteDataSource.getCodeList()
                    .doOnNext { localDataSource.saveCodeList(it) }

    fun getTimetable(theater: Theater, movie: Movie): Observable<Timetable> {
        return remoteDataSource.getTimetable(theater, movie)
                .map { it.copy(theater = theater) }
                .onErrorReturnItem(Timetable(theater = theater))
    }

    fun getVersion(): Observable<Version> =
            Observable.concat(
                    getVersionInMemory(),
                    getVersionFromNetwork())
                    .take(1)

    private fun getVersionInMemory(): Observable<Version> =
            localDataSource.getVersion()

    private fun getVersionFromNetwork() : Observable<Version> =
            remoteDataSource.getVersion()
                    .doOnNext { localDataSource.saveVersion(it) }
}
