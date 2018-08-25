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
import soup.movie.data.service.MoobApiService

class MoobDataSource(private val moobApiService: MoobApiService) : IMoobDataSource {

    override fun getNowList(request: NowMovieRequest): Single<NowMovieResponse> =
            moobApiService.getNowList()

    override fun getPlanList(request: PlanMovieRequest): Single<PlanMovieResponse> =
            moobApiService.getPlanList()

    override fun getCodeList(request: CodeRequest): Single<CodeResponse> =
            moobApiService.getCodeList()

    override fun getTimeTableList(request: TimeTableRequest): Single<TimeTableResponse> =
            moobApiService.getTimeTableList(request.theaterCode, request.movieCode)
}
