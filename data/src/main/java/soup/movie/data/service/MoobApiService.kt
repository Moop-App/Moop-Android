package soup.movie.data.service

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import soup.movie.data.response.CodeResponse
import soup.movie.data.response.NowMovieResponse
import soup.movie.data.response.PlanMovieResponse
import soup.movie.data.response.TimeTableResponse

interface MoobApiService {

    // 현재상영작
    @GET("v1/cgv/now/asTicketRate/nowOnly.json")
    fun getNowList(): Observable<NowMovieResponse>

    // 개봉예정작
    @GET("v1/cgv/plan/asOpenDate.json")
    fun getPlanList(): Observable<PlanMovieResponse>

    // 공통코드
    @GET("v1/code.json")
    fun getCodeList(): Observable<CodeResponse>

    // 상영시간표
    @GET("v1/cgv/timetable/asMovie/{tc}/{mc}.json")
    fun getTimeTableList(
            @Path("tc") theater: String,
            @Path("mc") movie: String): Observable<TimeTableResponse>

    companion object {

        const val API_BASE_URL = "https://moob-api.firebaseio.com/"
    }
}
