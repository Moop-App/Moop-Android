package soup.movie.data.source.remote

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import soup.movie.data.model.Version
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

    override fun getVersion(pkgName: String, defaultVersion: String): Observable<Version> =
            Observable.fromCallable {
                Version(getMarketVersionName(pkgName) ?: defaultVersion)
            }.subscribeOn(Schedulers.io())

    private fun getMarketVersionName(pkgName: String): String? {
        return try {
            Jsoup.connect("https://play.google.com/store/apps/details?id=$pkgName&hl=it")
                    .timeout(10000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first()
                    .ownText()
        } catch (e: Exception) {
            null
        }

    }
}
