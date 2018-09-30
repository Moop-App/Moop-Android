package soup.movie.ui.detail.timetable

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import soup.movie.data.MoobRepository
import soup.movie.data.helper.localDate
import soup.movie.data.helper.today
import soup.movie.data.model.*
import soup.movie.data.model.request.TimeTableRequest
import soup.movie.settings.impl.TheaterSetting
import soup.movie.ui.BasePresenter
import soup.movie.util.toObservable

class TimetablePresenter(private val moobRepository: MoobRepository,
                         private val theaterSetting: TheaterSetting) :
        BasePresenter<TimetableContract.View>(), TimetableContract.Presenter {

    private val movieSubject: BehaviorSubject<Movie> = BehaviorSubject.create()
    private val dateSubject = BehaviorSubject.createDefault(ScreeningDate(today()))
    private val timetableSubject: BehaviorSubject<TimeTable> = BehaviorSubject.create()
    private val theaterIdSubject: BehaviorSubject<String> = BehaviorSubject.create()
    private val theaterListSubject: BehaviorSubject<List<TheaterWithTimetable>> =
            BehaviorSubject.create()

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        with(disposable) {
            add(theaterSetting.asObservable()
                    .doOnNext {
                        theaterIdSubject.onNext(it.firstOrNull()?.code ?: Theater.NO_ID)
                    }
                    .subscribe())

            add(Observables.combineLatest(
                    theaterIdSubject.distinctUntilChanged(),
                    movieSubject.distinctUntilChanged())
                    .flatMap { getTimetable(it.second, it.first) }
                    .subscribe { timetableSubject.onNext(it) })

            add(Observables.combineLatest(
                    theaterSetting.asObservable(),
                    theaterIdSubject.distinctUntilChanged())
                    .map { (theaters, selectedId) ->
                        theaters.map { TheaterWithTimetable(it, selected = it.code == selectedId) }
                    }
                    .onErrorReturnItem(emptyList())
                    .subscribe { theaterListSubject.onNext(it) })

            add(Observables.combineLatest(
                    dateSubject.distinctUntilChanged(),
                    theaterListSubject,
                    timetableSubject)
                    .flatMap { (date, theaters, timetable) ->
                        if (theaters.isEmpty()) {
                            getEmptyViewState()
                        } else {
                            getViewState(date, theaters, timetable)
                        }
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { view?.render(it) })
        }
    }

    override fun requestData(movie: Movie) {
        movieSubject.onNext(movie)
    }

    override fun onItemClick(item: ScreeningDate) {
        dateSubject.onNext(item)
    }

    override fun onItemClick(item: TheaterWithTimetable) {
        theaterIdSubject.onNext(item.theater.code)
    }

    private fun getEmptyViewState(): Observable<TimetableViewState> {
        //TODO: Please inject disabled Date list
        return TimetableViewState(emptyList(), emptyList())
                .toObservable()
    }

    private fun getTimetable(movie: Movie, theaterId: String): Observable<TimeTable> {
        return moobRepository.getTimeTableList(TimeTableRequest(theaterId, movie.id))
                .map { it.timeTable }
                .onErrorReturnItem(TimeTable())
    }

    private fun getViewState(date: ScreeningDate,
                             theaters: List<TheaterWithTimetable>,
                             timeTable: TimeTable): Observable<TimetableViewState> {
        return Observable.just(TimetableViewState(
                timeTable.toScreeningDateList(date),
                theaters.injectTimeList(date, timeTable)))
    }

    private fun TimeTable.toScreeningDateList(date: ScreeningDate): List<ScreeningDate> {
        return dateList.map {
            ScreeningDate(it.localDate(), enabled = true, selected = it.localDate() == date.date)
        }
    }

    private fun List<TheaterWithTimetable>.injectTimeList(
            date: ScreeningDate, timeTable: TimeTable): List<TheaterWithTimetable> {
        val timeList = timeTable.dateList
                .find { it.localDate() == date.date }
                ?.timeList
                ?: emptyList()
        return map {
            if (it.selected) {
                it.copy(timeList = timeList)
            } else {
                it
            }
        }
    }
}
