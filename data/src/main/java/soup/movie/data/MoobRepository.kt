package soup.movie.data

import io.reactivex.Single
import soup.movie.data.model.*

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
