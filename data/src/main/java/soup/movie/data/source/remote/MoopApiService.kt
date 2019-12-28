package soup.movie.data.source.remote

import retrofit2.http.GET
import retrofit2.http.Path
import soup.movie.data.model.MovieDetail
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse

interface MoopApiService {

    // 현재상영작
    @GET("now.json")
    suspend fun getNowMovieList(): MovieListResponse

    // 개봉예정작
    @GET("plan.json")
    suspend fun getPlanMovieList(): MovieListResponse

    // 영화 상세정보
    @GET("detail/{movieId}.json")
    suspend fun getMovieDetail(
        @Path("movieId") movieId: String
    ): MovieDetail

    // 공통코드
    @GET("code.json")
    suspend fun getCodeList(): CodeResponse
}
