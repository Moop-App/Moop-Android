package soup.movie.data.source.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.model.response.TimetableResponse

interface MoopApiService {

    // 현재상영작
    @GET("cgv/now.json")
    fun getNowMovieList(): Observable<MovieListResponse>

    // 개봉예정작
    @GET("cgv/plan.json")
    fun getPlanMovieList(): Observable<MovieListResponse>

    // 공통코드
    @GET("code.json")
    fun getCodeList(): Observable<CodeResponse>

    // 상영시간표
    @GET("timetable/cgv/asMovie/{tc}/{mc}.json")
    fun getCgvTimetable(
            @Path("tc") theater: String,
            @Path("mc") movie: String): Observable<TimetableResponse>
}
