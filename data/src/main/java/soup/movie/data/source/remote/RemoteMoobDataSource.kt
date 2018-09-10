package soup.movie.data.source.remote

import io.reactivex.Observable
import soup.movie.data.model.request.TimeTableRequest
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.model.response.TimeTableResponse
import soup.movie.data.source.MoobDataSource

class RemoteMoobDataSource(private val moobApiService: MoobApiService) : MoobDataSource {

    override fun getNowList(): Observable<MovieListResponse> =
            moobApiService.getNowMovieList()

    override fun getPlanList(): Observable<MovieListResponse> =
            moobApiService.getPlanMovieList()

    override fun getCodeList(): Observable<CodeResponse> =
            moobApiService.getCodeList()

    override fun getTimeTableList(request: TimeTableRequest): Observable<TimeTableResponse> =
            moobApiService.getTimeTableList(request.theaterCode, request.movieCode)
}
