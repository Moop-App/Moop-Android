package soup.movie.data.source.remote

import io.reactivex.Observable
import retrofit2.http.GET
import soup.movie.data.model.Movie
import soup.movie.data.model.response.CodeResponse

interface MoopApiService {

    // 현재상영작
    @GET("v1/now/list.json")
    fun getNowMovieList(): Observable<List<Movie>>

    // 개봉예정작
    @GET("v1/plan/list.json")
    fun getPlanMovieList(): Observable<List<Movie>>

    // 공통코드
    @GET("v2/code.json")
    suspend fun getCodeList(): CodeResponse
}
