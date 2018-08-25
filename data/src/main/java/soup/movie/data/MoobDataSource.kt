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
import soup.movie.data.service.MoobApiService

class MoobDataSource(private val moobApiService: MoobApiService) : IMoobDataSource {

    override fun getNowList(request: NowMovieRequest): Observable<NowMovieResponse> =
            moobApiService.getNowList()

    override fun getPlanList(request: PlanMovieRequest): Observable<PlanMovieResponse> =
            moobApiService.getPlanList()

    override fun getCodeList(request: CodeRequest): Observable<CodeResponse> =
            moobApiService.getCodeList()

    override fun getTimeTableList(request: TimeTableRequest): Observable<TimeTableResponse> =
            moobApiService.getTimeTableList(request.theaterCode, request.movieCode)
}
