package soup.movie.data.source.remote

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import soup.movie.data.model.MovieDetail
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.util.OkHttpInterceptors.HEADER_USE_CACHE

interface MoopApiService {

    // 현재상영작

    @GET("now.json")
    suspend fun getNowMovieList(): MovieListResponse

    @Headers(HEADER_USE_CACHE)
    @GET("now/lastUpdateTime.json")
    suspend fun getNowLastUpdateTime(): Long

    // 개봉예정작

    @GET("plan.json")
    suspend fun getPlanMovieList(): MovieListResponse

    @Headers(HEADER_USE_CACHE)
    @GET("plan/lastUpdateTime.json")
    suspend fun getPlanLastUpdateTime(): Long

    // 영화 상세정보
    @GET("detail/{movieId}.json")
    suspend fun getMovieDetail(
        @Path("movieId") movieId: String
    ): MovieDetail

    // 공통코드
    @GET("code.json")
    suspend fun getCodeList(): CodeResponse
}
