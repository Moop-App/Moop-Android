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

interface IMoobDataSource {

    fun getNowList(request: NowMovieRequest): Single<NowMovieResponse>

    fun getPlanList(request: PlanMovieRequest): Single<PlanMovieResponse>

    fun getCodeList(request: CodeRequest): Single<CodeResponse>

    fun getTimeTableList(request: TimeTableRequest): Single<TimeTableResponse>
}
