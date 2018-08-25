package soup.movie.data

import io.reactivex.Observable
import soup.movie.data.request.CodeRequest
import soup.movie.data.request.NowMovieRequest
import soup.movie.data.request.PlanMovieRequest
import soup.movie.data.request.TimeTableRequest
import soup.movie.data.response.CodeResponse
import soup.movie.data.response.NowMovieResponse
import soup.movie.data.response.PlanMovieResponse
import soup.movie.data.response.TimeTableResponse

class MoobRepository(
        private val remoteDataSource: IMoobDataSource) : IMoobDataSource {

    override fun getNowList(request: NowMovieRequest): Observable<NowMovieResponse> {
        return remoteDataSource.getNowList(request)
    }

    override fun getPlanList(request: PlanMovieRequest): Observable<PlanMovieResponse> {
        return remoteDataSource.getPlanList(request)
    }

    override fun getCodeList(request: CodeRequest): Observable<CodeResponse> {
        return remoteDataSource.getCodeList(request)
    }

    override fun getTimeTableList(request: TimeTableRequest): Observable<TimeTableResponse> {
        return remoteDataSource.getTimeTableList(request)
    }
}
