package soup.movie.ui.main.movie

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import soup.movie.data.model.Movie
import soup.movie.settings.impl.AgeFilterSetting
import soup.movie.settings.impl.TheaterFilterSetting
import soup.movie.ui.LegacyBasePresenter
import soup.movie.ui.main.movie.MovieListContract.Presenter
import soup.movie.ui.main.movie.MovieListContract.View
import soup.movie.ui.main.movie.MovieListViewState.*

abstract class MovieListPresenter(private val theaterFilterSetting: TheaterFilterSetting,
                                  private val ageFilterSetting: AgeFilterSetting) :
        LegacyBasePresenter<View>(), Presenter {

    private val refreshRelay = BehaviorRelay.createDefault(false)

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(Observables.combineLatest(
                refreshRelay,
                theaterFilterSetting.asObservable(),
                ageFilterSetting.asObservable().distinctUntilChanged())
                .subscribeOn(Schedulers.io())
                .switchMap { (clearCache, theaterFilter, ageFilter) -> getMovieList(clearCache)
                        .map { it -> it
                                .asSequence()
                                .filter {
                                    (theaterFilter.hasCgv() and it.isScreeningAtCgv()) or
                                    (theaterFilter.hasLotteCinema() and it.isScreeningAtLotteCinema()) or
                                    (theaterFilter.hasMegabox() and it.isScreeningAtMegabox())
                                }
                                .filter {
                                    (ageFilter.hasAll() and it.isScreeningForAgeAll()) or
                                    (ageFilter.has12() and it.isScreeningOverAge12()) or
                                    (ageFilter.has15() and it.isScreeningOverAge15()) or
                                    (ageFilter.has19() and it.isScreeningOverAge19())
                                }
                                .toList()
                        }
                        .map { DoneState(it) }
                        .cast(MovieListViewState::class.java)
                        .startWith(LoadingState)
                        .onErrorReturnItem(ErrorState) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    override fun refresh() {
        refreshRelay.accept(true)
    }

    abstract fun getMovieList(clearCache: Boolean): Observable<List<Movie>>
}
