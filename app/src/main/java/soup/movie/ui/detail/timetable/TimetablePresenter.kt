package soup.movie.ui.detail.timetable

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import soup.movie.data.MoopRepository
import soup.movie.data.helper.localDate
import soup.movie.data.helper.toWeek
import soup.movie.data.helper.today
import soup.movie.data.helper.until
import soup.movie.data.model.*
import soup.movie.data.util.firstOr
import soup.movie.settings.impl.TheatersSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.detail.timetable.TimetableViewState.DoneState
import soup.movie.ui.detail.timetable.TimetableViewState.LoadingState
import java.util.concurrent.TimeUnit

class TimetablePresenter(private val repository: MoopRepository,
                         private val theatersSetting: TheatersSetting) :
        BasePresenter<TimetableContract.View>(), TimetableContract.Presenter {

    private val movieSubject: BehaviorSubject<Movie> = BehaviorSubject.create()
    private val dateSubject = BehaviorSubject.createDefault(ScreeningDate(today()))
    private val theaterSubject: BehaviorSubject<Theater> = BehaviorSubject.createDefault(Theater.NONE)

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        with(disposable) {
            add(theatersSetting.asObservable()
                    .map { it.firstOr(Theater.NONE) }
                    .subscribe { theaterSubject.onNext(it) })

            add(getTimetableViewState()
                    .startWith(LoadingState)
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

    private fun getTimetableViewState(): Observable<TimetableViewState> {
        return Observables.combineLatest(
                getOriginTheaterListObservable(),
                getTimetableObservable(),
                dateSubject.distinctUntilChanged())
                //TODO: Instead of debounce(), update if theater is changed.
                .debounce(300, TimeUnit.MILLISECONDS)
                .map { (originTheaterList, timetable, screeningDate) ->
                    val screeningDateList = timetable.dateList
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
                                            selected = it == screeningDate.date)
                                }
                            }
                    val hallList = timetable.dateList
                            .find { it.localDate() == screeningDate.date }
                            ?.hallList ?: emptyList()
                    val theaterList = originTheaterList
                            .map {
                                if (it.selected and (it.theater == timetable.theater)) {
                                    it.copy(hallList = hallList)
                                } else {
                                    it
                                }
                            }
                    Pair(screeningDateList, theaterList)
                }
                .map { DoneState(it.first, it.second) }
    }

    private fun getOriginTheaterListObservable(): Observable<List<TheaterWithTimetable>> {
        return Observables.combineLatest(
                theatersSetting.asObservable(),
                theaterSubject.distinctUntilChanged())
                .map { (theaterList, selectedTheater) ->
                    theaterList.map {
                        TheaterWithTimetable(
                                theater = it,
                                selected = it == selectedTheater)
                    }
                }
                .onErrorReturnItem(emptyList())
    }

    private fun getTimetableObservable(): Observable<Timetable> {
        return Observables.combineLatest(
                theaterSubject.distinctUntilChanged(),
                movieSubject.distinctUntilChanged())
                .flatMap { (theater, movie) ->
                    repository.getTimetable(theater, movie)
                }
    }
}
