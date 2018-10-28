package soup.movie.ui.main.movie

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import soup.movie.data.model.Movie
import soup.movie.settings.impl.MovieFilterSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.main.movie.MovieListContract.Presenter
import soup.movie.ui.main.movie.MovieListContract.View
import soup.movie.ui.main.movie.MovieListViewState.*

abstract class MovieListPresenter(private val filterSetting: MovieFilterSetting) :
        BasePresenter<View>(), Presenter {

    private val refreshRelay = BehaviorRelay.createDefault(false)

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(Observables.combineLatest(
                refreshRelay,
                filterSetting.asObservable())
                .subscribeOn(Schedulers.io())
                .switchMap { (clearCache, movieFilter) -> getMovieList(clearCache)
                        .map { it -> it
                                .asSequence()
                                .filter {
                                    (movieFilter.hasCgv() and it.isScreeningAtCgv()) or
                                    (movieFilter.hasLotteCinema() and it.isScreeningAtLotteCinema()) or
                                    (movieFilter.hasMegabox() and it.isScreeningAtMegabox())
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
