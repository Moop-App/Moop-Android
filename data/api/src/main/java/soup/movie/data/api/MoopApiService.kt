package soup.movie.data.api

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import soup.movie.data.api.internal.OkHttpInterceptors.HEADER_USE_CACHE
import soup.movie.data.api.response.MovieDetailResponse
import soup.movie.data.api.response.MovieListResponse
import soup.movie.data.api.response.TheaterAreaGroupResponse

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
    @Headers(HEADER_USE_CACHE)
    @GET("detail/{movieId}.json")
    suspend fun getMovieDetail(@Path("movieId") movieId: String): MovieDetailResponse

    // 공통코드
    @GET("code.json")
    suspend fun getCodeList(): TheaterAreaGroupResponse
}
