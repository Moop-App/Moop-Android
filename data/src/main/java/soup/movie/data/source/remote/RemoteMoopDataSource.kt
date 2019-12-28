package soup.movie.data.source.remote

import soup.movie.data.model.MovieDetail
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.source.MoopDataSource

class RemoteMoopDataSource(private val moopApi: MoopApiService) : MoopDataSource {

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

    suspend fun getMovieDetail(movieId: String): MovieDetail {
        return moopApi.getMovieDetail(movieId)
    }

    suspend fun getCodeList(): CodeResponse {
        return moopApi.getCodeList()
    }
}
