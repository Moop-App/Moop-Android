package soup.movie.data.source.remote

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import soup.movie.data.model.Movie
import soup.movie.data.model.Theater
import soup.movie.data.model.Timetable
import soup.movie.data.model.Version
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.source.MoobDataSource
import soup.movie.data.util.toAnObservable

class RemoteMoobDataSource(private val moobApiService: MoobApiService) : MoobDataSource {

    override fun getNowList(): Observable<MovieListResponse> =
            moobApiService.getNowMovieList()

    override fun getPlanList(): Observable<MovieListResponse> =
            moobApiService.getPlanMovieList()

    override fun getCodeList(): Observable<CodeResponse> =
            moobApiService.getCodeList()

    override fun getTimetable(theater: Theater, movie: Movie): Observable<Timetable> =
            moobApiService.getTimetable(theater.code, movie.id)
                    .map { it.timetable }

    override fun getVersion(pkgName: String, defaultVersion: String): Observable<Version> =
            Version(defaultVersion)
                    .toAnObservable()
                    .subscribeOn(Schedulers.io())
}
