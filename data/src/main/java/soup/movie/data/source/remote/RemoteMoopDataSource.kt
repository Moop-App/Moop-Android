package soup.movie.data.source.remote

import soup.movie.data.model.MovieDetail
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.source.MoopDataSource

class RemoteMoopDataSource(private val moopApiService: MoopApiService) : MoopDataSource {

    suspend fun getNowList(): MovieListResponse {
        return moopApiService.getNowMovieList()
    }

    suspend fun getPlanList(): MovieListResponse {
        return moopApiService.getPlanMovieList()
    }

    suspend fun getMovieDetail(movieId: String): MovieDetail {
        return moopApiService.getMovieDetail(movieId)
    }

    suspend fun getCodeList(): CodeResponse {
        return moopApiService.getCodeList()
    }
}
