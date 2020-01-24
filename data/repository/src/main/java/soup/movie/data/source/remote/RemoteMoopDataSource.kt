package soup.movie.data.source.remote

import soup.movie.data.model.response.MovieDetailResponse
import soup.movie.data.model.response.TheaterAreaGroupResponse
import soup.movie.data.model.response.MovieListResponse

class RemoteMoopDataSource(
    private val moopApi: MoopApiService
) {

    suspend fun getNowList(): MovieListResponse {
        return moopApi.getNowMovieList()
    }

    suspend fun getNowLastUpdateTime(): Long {
        return moopApi.getNowLastUpdateTime()
    }

    suspend fun getPlanList(): MovieListResponse {
        return moopApi.getPlanMovieList()
    }

    suspend fun getPlanLastUpdateTime(): Long {
        return moopApi.getPlanLastUpdateTime()
    }

    suspend fun getMovieDetail(movieId: String): MovieDetailResponse {
        return moopApi.getMovieDetail(movieId)
    }

    suspend fun getCodeList(): TheaterAreaGroupResponse {
        return moopApi.getCodeList()
    }
}
