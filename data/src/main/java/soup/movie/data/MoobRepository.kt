package soup.movie.data

import io.reactivex.Single
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

    override fun getNowList(request: NowMovieRequest): Single<NowMovieResponse> {
        return remoteDataSource.getNowList(request)
    }

    override fun getPlanList(request: PlanMovieRequest): Single<PlanMovieResponse> {
        return remoteDataSource.getPlanList(request)
    }

    override fun getCodeList(request: CodeRequest): Single<CodeResponse> {
        return remoteDataSource.getCodeList(request)
    }

    override fun getTimeTableList(request: TimeTableRequest): Single<TimeTableResponse> {
        return remoteDataSource.getTimeTableList(request)
    }
}
