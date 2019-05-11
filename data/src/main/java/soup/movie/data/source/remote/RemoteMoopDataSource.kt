package soup.movie.data.source.remote

import io.reactivex.Observable
import soup.movie.data.model.Movie
import soup.movie.data.model.Version
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.source.MoopDataSource

class RemoteMoopDataSource(private val moopApiService: MoopApiService) : MoopDataSource {

    override fun getNowList(): Observable<MovieListResponse> =
        moopApiService.getNowMovieList()
            .map { it.withTimestamp() }

    override fun getPlanList(): Observable<MovieListResponse> =
        moopApiService.getPlanMovieList()
            .map { it.withTimestamp() }

    private fun List<Movie>.withTimestamp(): MovieListResponse =
        MovieListResponse(System.currentTimeMillis(), this)

    override fun getCodeList(): Observable<CodeResponse> =
        moopApiService.getCodeList()

    override fun getVersion(): Observable<Version> =
        moopApiService.getVersion().map { it.android }
}
