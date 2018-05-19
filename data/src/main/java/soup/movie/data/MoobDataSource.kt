package soup.movie.data

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import soup.movie.data.model.CodeRequest
import soup.movie.data.model.CodeResponse
import soup.movie.data.model.NowMovieRequest
import soup.movie.data.model.NowMovieResponse
import soup.movie.data.model.PlanMovieRequest
import soup.movie.data.model.PlanMovieResponse
import soup.movie.data.model.TimeTableRequest
import soup.movie.data.model.TimeTableResponse
import soup.movie.data.service.MoobApiService

class MoobDataSource(private val moobApiService: MoobApiService) : IMoobDataSource {

    override fun getNowList(request: NowMovieRequest): Single<NowMovieResponse> {
        return moobApiService.nowList
                .subscribeOn(Schedulers.io())
    }

    override fun getPlanList(request: PlanMovieRequest): Single<PlanMovieResponse> {
        return moobApiService.planList
                .subscribeOn(Schedulers.io())
    }

    override fun getCodeList(request: CodeRequest): Single<CodeResponse> {
        return moobApiService.codeList
                .subscribeOn(Schedulers.io())
    }

    override fun getTimeTableList(request: TimeTableRequest): Single<TimeTableResponse> {
        return moobApiService.getTimeTableList(request.theaterCode, request.movieCode)
                .subscribeOn(Schedulers.io())
    }
}
