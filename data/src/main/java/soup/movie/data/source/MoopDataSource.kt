package soup.movie.data.source

import io.reactivex.Observable
import soup.movie.data.model.Version
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse

interface MoopDataSource {

    fun getNowList(): Observable<MovieListResponse>

    fun getPlanList(): Observable<MovieListResponse>

    fun getCodeList(): Observable<CodeResponse>

    fun getVersion(): Observable<Version>
}
