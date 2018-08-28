package soup.movie.ui.detail.timetable

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.subjects.BehaviorSubject
import soup.movie.data.MoobRepository
import soup.movie.data.model.Movie
import soup.movie.data.model.TimeTable
import soup.movie.data.request.TimeTableRequest
import soup.movie.settings.TheaterSetting
import soup.movie.ui.BasePresenter

class TimeTablePresenter(
        private val moobRepository: MoobRepository,
        private val theaterSetting: TheaterSetting)
    : BasePresenter<TimeTableContract.View>(), TimeTableContract.Presenter {

    private val movieSubject: BehaviorSubject<Movie> = BehaviorSubject.create()

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(theaterSetting
                .asObservable()
                .flatMap {
                    when {
                        it.isEmpty() -> Observable.just(TimeTable())
                        else -> getTimeTableBy(it[0].code, movieSubject.value!!.id)
                    }
                }
                .map { TimeTableViewState.DoneState(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    override fun requestData(movie: Movie) {
        movieSubject.onNext(movie)
    }

    private fun getTimeTableBy(theaterId: String, movieId: String): Observable<TimeTable> {
        return moobRepository.getTimeTableList(TimeTableRequest(theaterId, movieId))
                .map { it.timeTable }
                .onErrorReturnItem(TimeTable())
    }
}
