package soup.movie.ui.detail.timetable

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.subjects.BehaviorSubject
import soup.movie.data.MoobRepository
import soup.movie.data.model.Movie
import soup.movie.data.request.TimeTableRequest
import soup.movie.settings.impl.TheaterSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.detail.timetable.TimetableViewState.*

class TimetablePresenter(private val moobRepository: MoobRepository,
                         private val theaterSetting: TheaterSetting) :
        BasePresenter<TimetableContract.View>(), TimetableContract.Presenter {

    private val movieSubject: BehaviorSubject<Movie> = BehaviorSubject.create()

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(movieSubject
                .flatMap {
                    val theaters = theaterSetting.get()
                    when {
                        theaters.isEmpty() -> Observable.just(NoTheaterState)
                        else -> getTimeTableBy(theaters[0].code, it.id)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    override fun requestData(movie: Movie) {
        movieSubject.onNext(movie)
    }

    private fun getTimeTableBy(theaterId: String, movieId: String): Observable<TimetableViewState> {
        return moobRepository.getTimeTableList(TimeTableRequest(theaterId, movieId))
                .map<TimetableViewState> { DataState(it.timeTable) }
                .onErrorReturnItem(NoResultState)
    }
}
