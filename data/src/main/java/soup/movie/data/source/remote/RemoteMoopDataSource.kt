package soup.movie.data.source.remote

import io.reactivex.Observable
import soup.movie.data.model.Movie
import soup.movie.data.model.Theater
import soup.movie.data.model.Theater.Companion.TYPE_CGV
import soup.movie.data.model.Theater.Companion.TYPE_LOTTE
import soup.movie.data.model.Theater.Companion.TYPE_MEGABOX
import soup.movie.data.model.Timetable
import soup.movie.data.model.Version
import soup.movie.data.model.response.CodeResponse
import soup.movie.data.model.response.MovieListResponse
import soup.movie.data.source.MoopDataSource
import soup.movie.data.util.toAnObservable

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

    override fun getTimetable(theater: Theater, movie: Movie): Observable<Timetable> {
        when (theater.type) {
            TYPE_CGV -> {
                val cgv = movie.cgv
                if (cgv != null) {
                    return moopApiService
                            .getCgvTimetable(theater.code, cgv.id)
                            .map { it.timetable }
                }
            }
            TYPE_LOTTE -> {
                val lotte = movie.lotte
                if (lotte != null) {
                    return moopApiService
                            .getLotteTimetable(theater.code, lotte.id)
                            .map { it.timetable }
                }
            }
            TYPE_MEGABOX -> {
                val megabox = movie.megabox
                if (megabox != null) {
                    return moopApiService
                            .getMegaboxTimetable(theater.code, megabox.id)
                            .map { it.timetable }
                }
            }
        }
        return Timetable().toAnObservable()
    }

    override fun getVersion(): Observable<Version> =
            moopApiService.getVersion().map { it.android }
}
