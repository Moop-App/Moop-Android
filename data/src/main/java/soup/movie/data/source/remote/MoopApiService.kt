package soup.movie.data.source.remote

import io.reactivex.Observable
import retrofit2.http.GET
import soup.movie.data.model.Movie
import soup.movie.data.model.response.CodeResponse

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
}
