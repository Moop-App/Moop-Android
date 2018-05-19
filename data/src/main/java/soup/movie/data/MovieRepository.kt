package soup.movie.data

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.Single
import soup.movie.data.model.CodeRequest
import soup.movie.data.model.CodeResponse
import soup.movie.data.model.NowMovieRequest
import soup.movie.data.model.NowMovieResponse
import soup.movie.data.model.PlanMovieRequest
import soup.movie.data.model.PlanMovieResponse
import soup.movie.data.model.TimeTableRequest
import soup.movie.data.model.TimeTableResponse

@Singleton
class MovieRepository @Inject
constructor(private val remoteDataSource: IMoobDataSource) : IMoobDataSource {

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
