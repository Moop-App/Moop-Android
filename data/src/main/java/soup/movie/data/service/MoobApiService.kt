package soup.movie.data.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import soup.movie.data.model.CodeResponse
import soup.movie.data.model.NowMovieResponse
import soup.movie.data.model.PlanMovieResponse
import soup.movie.data.model.TimeTableResponse

interface MoobApiService {

    // 현재상영작
    @get:GET("v1/cgv/now/asTicketRate/nowOnly.json")
    val nowList: Single<NowMovieResponse>

    // 개봉예정작
    @get:GET("v1/cgv/plan/asOpenDate.json")
    val planList: Single<PlanMovieResponse>

    // 공통코드
    @get:GET("v1/code.json")
    val codeList: Single<CodeResponse>

    // 상영시간표
    @GET("v1/cgv/timetable/asMovie/{tc}/{mc}.json")
    fun getTimeTableList(@Path("tc") theater: String,
                         @Path("mc") movie: String): Single<TimeTableResponse>

    companion object {
        val API_BASE_URL = "https://moob-api.firebaseio.com/"
    }
}
