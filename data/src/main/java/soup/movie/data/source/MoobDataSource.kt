package soup.movie.data.source

import io.reactivex.Observable
import soup.movie.data.model.Version
import soup.movie.data.model.request.TimeTableRequest
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.model.response.TimeTableResponse

interface MoobDataSource {

    fun getNowList(): Observable<MovieListResponse>

    fun getPlanList(): Observable<MovieListResponse>

    fun getCodeList(): Observable<CodeResponse>

    fun getTimeTableList(request: TimeTableRequest): Observable<TimeTableResponse>

    fun getVersion(pkgName: String, defaultVersion: String): Observable<Version>
}
