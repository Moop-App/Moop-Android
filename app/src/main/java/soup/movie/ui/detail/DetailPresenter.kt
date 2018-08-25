package soup.movie.ui.detail

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import soup.movie.data.MoobRepository
import soup.movie.data.model.Movie
import soup.movie.data.model.Theater
import soup.movie.data.model.TimeTable
import soup.movie.data.model.Trailer
import soup.movie.data.request.TimeTableRequest
import soup.movie.settings.TheaterSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.detail.DetailContract.Presenter
import soup.movie.ui.detail.DetailContract.View
import soup.movie.ui.detail.DetailViewState.DoneState
import soup.movie.ui.detail.DetailViewState.LoadingState

class DetailPresenter(
        private val moobRepository: MoobRepository,
        private val theaterSetting: TheaterSetting)
    : BasePresenter<View>(), Presenter {

    override fun requestData(movie: Movie) {
        register(Observable.combineLatest(
                getTimeTableObservable(theaterSetting.get(), movie.id),
                getTrailerListObservable(movie),
                BiFunction(::DoneState))
                .startWith { LoadingState }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view!!.render(it) })
    }

    private fun getTimeTableObservable(
            theaters: List<Theater>, movieId: String): Observable<TimeTable> {
        return if (theaters.isEmpty()) {
            Observable.just(TimeTable())
        } else {
            getTimeTableBy(theaters[0].code, movieId)
        }
    }

    private fun getTimeTableBy(theaterId: String, movieId: String): Observable<TimeTable> {
        return moobRepository.getTimeTableList(TimeTableRequest(theaterId, movieId))
                .map { it.timeTable }
                .onErrorReturnItem(TimeTable())
    }

    private fun getTrailerListObservable(movie: Movie): Observable<List<Trailer>> {
        return Observable.just(movie.trailers)
                .onErrorReturnItem(emptyList())
    }
}
