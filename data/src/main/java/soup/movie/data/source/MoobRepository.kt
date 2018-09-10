package soup.movie.data.source

import io.reactivex.Observable
import soup.movie.data.model.request.TimeTableRequest
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.model.response.TimeTableResponse
import soup.movie.data.source.local.LocalMoobDataSource
import soup.movie.data.source.remote.RemoteMoobDataSource

class MoobRepository(private val localDataSource: LocalMoobDataSource,
                     private val remoteDataSource: RemoteMoobDataSource) : MoobDataSource {

    override fun getNowList(): Observable<MovieListResponse> {
        return Observable.concat(
                localDataSource.getNowList(),
                remoteDataSource.getNowList()
                        .doOnNext { localDataSource.saveNowList(it) })
                .take(1)
    }

    override fun getPlanList(): Observable<MovieListResponse> {
        return Observable.concat(
                localDataSource.getPlanList(),
                remoteDataSource.getPlanList()
                        .doOnNext { localDataSource.savePlanList(it) })
                .take(1)
    }

    override fun getCodeList(): Observable<CodeResponse> {
        return remoteDataSource.getCodeList()
    }

    override fun getTimeTableList(request: TimeTableRequest): Observable<TimeTableResponse> {
        return remoteDataSource.getTimeTableList(request)
    }
}
