package soup.movie.data

import io.reactivex.Single
import soup.movie.data.model.CodeRequest
import soup.movie.data.model.CodeResponse
import soup.movie.data.model.NowMovieRequest
import soup.movie.data.model.NowMovieResponse
import soup.movie.data.model.PlanMovieRequest
import soup.movie.data.model.PlanMovieResponse
import soup.movie.data.model.TimeTableRequest
import soup.movie.data.model.TimeTableResponse

interface IMoobDataSource {

    fun getNowList(request: NowMovieRequest): Single<NowMovieResponse>

    fun getPlanList(request: PlanMovieRequest): Single<PlanMovieResponse>

    fun getCodeList(request: CodeRequest): Single<CodeResponse>

    fun getTimeTableList(request: TimeTableRequest): Single<TimeTableResponse>
}
