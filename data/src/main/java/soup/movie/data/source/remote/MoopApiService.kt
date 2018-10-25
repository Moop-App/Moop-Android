package soup.movie.data.source.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import soup.movie.data.model.Movie
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.TimetableResponse
import soup.movie.data.model.response.VersionResponse

interface MoopApiService {

    // 현재상영작
    @GET("now/list.json")
    fun getNowMovieList(): Observable<List<Movie>>

    // 개봉예정작
    @GET("plan/list.json")
    fun getPlanMovieList(): Observable<List<Movie>>

    // 공통코드
    @GET("code.json")
    fun getCodeList(): Observable<CodeResponse>

    // 상영시간표
    @GET("timetable/cgv/asMovie/{tc}/{mc}.json")
    fun getCgvTimetable(
            @Path("tc") theater: String,
            @Path("mc") movie: String): Observable<TimetableResponse>

    // 상영시간표
    @GET("timetable/lotte/asMovie/{tc}/{mc}.json")
    fun getLotteTimetable(
            @Path("tc") theater: String,
            @Path("mc") movie: String): Observable<TimetableResponse>

    // 상영시간표
    @GET("timetable/megabox/asMovie/{tc}/{mc}.json")
    fun getMegaboxTimetable(
            @Path("tc") theater: String,
            @Path("mc") movie: String): Observable<TimetableResponse>

    // 버전정보
    @GET("version.json")
    fun getVersion(): Observable<VersionResponse>
}
