package soup.movie.ui.detail.timetable

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import soup.movie.data.MoobRepository
import soup.movie.data.helper.localDate
import soup.movie.data.helper.toWeek
import soup.movie.data.helper.today
import soup.movie.data.model.*
import soup.movie.data.model.request.TimeTableRequest
import soup.movie.settings.impl.TheaterSetting
import soup.movie.ui.BasePresenter
import soup.movie.util.toObservable
import java.util.concurrent.TimeUnit

class TimetablePresenter(private val moobRepository: MoobRepository,
                         private val theaterSetting: TheaterSetting) :
        BasePresenter<TimetableContract.View>(), TimetableContract.Presenter {

    private val movieSubject: BehaviorSubject<Movie> = BehaviorSubject.create()
    private val dateSubject = BehaviorSubject.createDefault(ScreeningDate(today()))
    private val theaterIdSubject: BehaviorSubject<String> = BehaviorSubject.create()

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        with(disposable) {
            add(theaterSetting.asObservable()
                    .map { it.firstOrNull()?.code ?: Theater.NO_ID }
                    .subscribe { theaterIdSubject.onNext(it) })

            add(Observables.combineLatest(
                    getScreeningDateListObservable(),
                    getTheaterListObservable())
                    //TODO: Instead of debounce(), update if theater is changed.
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .flatMap { getViewState(it.first, it.second) }
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

    private fun getTimeTableObservable(): Observable<TimeTable> {
        return Observables.combineLatest(
                theaterIdSubject.distinctUntilChanged(),
                movieSubject.distinctUntilChanged())
                .flatMap { getTimetable(it.second, it.first) }
    }

    private fun getTimetable(movie: Movie, theaterId: String): Observable<TimeTable> {
        return moobRepository.getTimeTableList(TimeTableRequest(theaterId, movie.id))
                .map { it.timeTable }
                .onErrorReturnItem(TimeTable())
    }

    private fun getScreeningDateListObservable(): Observable<List<ScreeningDate>> {
        return Observables.combineLatest(
                getTimeTableObservable(),
                dateSubject.distinctUntilChanged(),
                ::mapToScreeningDateList)
    }

    private fun mapToScreeningDateList(timeTable: TimeTable, date: ScreeningDate): List<ScreeningDate> {
        return timeTable.dateList.run {
            if (isEmpty()) {
                today().toWeek().map {
                    ScreeningDate(
                            date = it,
                            enabled = false,
                            selected = it == date.date)
                }
            } else {
                map {
                    ScreeningDate(
                            date = it.localDate(),
                            enabled = true,
                            selected = it.localDate() == date.date)
                }
            }
        }
    }

    private fun getTheaterListObservable(): Observable<List<TheaterWithTimetable>> {
        return Observables.combineLatest(
                getOriginTheaterListObservable(),
                getTimeListObservable(),
                ::mapToTheaterList)
    }

    private fun getOriginTheaterListObservable(): Observable<List<TheaterWithTimetable>> {
        return Observables.combineLatest(
                theaterSetting.asObservable(),
                theaterIdSubject.distinctUntilChanged())
                .map { (theaters, selectedId) ->
                    theaters.map { TheaterWithTimetable(it, selected = it.code == selectedId) }
                }
                .onErrorReturnItem(emptyList())
    }

    private fun getTimeListObservable(): Observable<List<String>> {
        return Observables.combineLatest(
                getTimeTableObservable(),
                dateSubject.distinctUntilChanged())
                .map { (timeTable, date) ->
                    timeTable.dateList
                            .find { it.localDate() == date.date }
                            ?.timeList
                            ?: emptyList()
                }
    }

    private fun mapToTheaterList(theaters: List<TheaterWithTimetable>,
                                 timeList: List<String>): List<TheaterWithTimetable> {
        return theaters.map {
            if (it.selected) {
                it.copy(timeList = timeList)
            } else {
                it
            }
        }
    }

    private fun getViewState(
            screeningDateList: List<ScreeningDate>,
            theaterList: List<TheaterWithTimetable>): Observable<TimetableViewState> {
        return TimetableViewState(screeningDateList, theaterList)
                .toObservable()
    }
}
