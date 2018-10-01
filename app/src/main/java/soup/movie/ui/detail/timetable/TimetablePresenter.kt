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
import soup.movie.data.helper.until
import soup.movie.data.model.*
import soup.movie.data.util.firstOr
import soup.movie.settings.impl.TheaterSetting
import soup.movie.ui.BasePresenter
import java.util.concurrent.TimeUnit

class TimetablePresenter(private val moobRepository: MoobRepository,
                         private val theaterSetting: TheaterSetting) :
        BasePresenter<TimetableContract.View>(), TimetableContract.Presenter {

    private val movieSubject: BehaviorSubject<Movie> = BehaviorSubject.create()
    private val dateSubject = BehaviorSubject.createDefault(ScreeningDate(today()))
    private val theaterSubject: BehaviorSubject<Theater> = BehaviorSubject.create()

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        with(disposable) {
            add(theaterSetting.asObservable()
                    .map { it.firstOr(Theater.NONE) }
                    .subscribe { theaterSubject.onNext(it) })

            add(Observables.combineLatest(
                    getScreeningDateListObservable(),
                    getTheaterListObservable())
                    //TODO: Instead of debounce(), update if theater is changed.
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .map { TimetableViewState(it.first, it.second) }
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
        theaterSubject.onNext(item.theater)
    }

    private fun getTimetableObservable(): Observable<Timetable> {
        return Observables.combineLatest(
                theaterSubject.distinctUntilChanged(),
                movieSubject.distinctUntilChanged())
                .flatMap { getTimetable(it.first, it.second) }
    }

    private fun getTimetable(theater: Theater, movie: Movie): Observable<Timetable> {
        return moobRepository.getTimetable(theater, movie)
                .onErrorReturnItem(Timetable())
    }

    private fun getScreeningDateListObservable(): Observable<List<ScreeningDate>> {
        return Observables.combineLatest(
                getTimetableObservable(),
                dateSubject.distinctUntilChanged(),
                ::mapToScreeningDateList)
    }

    private fun mapToScreeningDateList(timetable: Timetable, date: ScreeningDate): List<ScreeningDate> {
        return timetable.dateList
                .map { it.localDate() }
                .run {
                    if (isEmpty()) {
                        today().toWeek()
                    } else {
                        first().until(last(), atLeast = 7)
                    }.map {
                        ScreeningDate(
                                date = it,
                                enabled = contains(it),
                                selected = it == date.date)
                    }
                }
    }

    private fun getTheaterListObservable(): Observable<List<TheaterWithTimetable>> {
        return Observables.combineLatest(
                getOriginTheaterListObservable(),
                getTimeListObservable())
                .map { (theaters, timeList) ->
                    theaters.map {
                        if (it.selected) {
                            it.copy(timeList = timeList)
                        } else {
                            it
                        }
                    }
                }
    }

    private fun getOriginTheaterListObservable(): Observable<List<TheaterWithTimetable>> {
        return Observables.combineLatest(
                theaterSetting.asObservable(),
                theaterSubject.map { it.code }.distinctUntilChanged())
                .map { (theaterList, selectedId) ->
                    theaterList.map {
                        TheaterWithTimetable(
                                theater = it,
                                selected = it.code == selectedId)
                    }
                }
                .onErrorReturnItem(emptyList())
    }

    private fun getTimeListObservable(): Observable<List<String>> {
        return Observables.combineLatest(
                getTimetableObservable(),
                dateSubject.distinctUntilChanged())
                .map { (timeTable, date) ->
                    timeTable.dateList
                            .find { it.localDate() == date.date }
                            ?.timeList
                            ?: emptyList()
                }
    }
}
