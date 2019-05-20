package soup.movie.ui.main.now

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import soup.movie.data.MoopRepository
import soup.movie.data.model.Movie
import soup.movie.settings.impl.AgeFilterSetting
import soup.movie.settings.impl.TheaterFilterSetting
import soup.movie.ui.BaseViewModel
import soup.movie.ui.main.movie.MovieListUiModel
import javax.inject.Inject

class NowViewModel @Inject constructor(
    theaterFilterSetting: TheaterFilterSetting,
    ageFilterSetting: AgeFilterSetting,
    private val repository: MoopRepository
) : BaseViewModel() {

    private val _uiModel = MutableLiveData<MovieListUiModel>()
    val uiModel: LiveData<MovieListUiModel>
        get() = _uiModel

    private val refreshRelay = BehaviorRelay.createDefault(false)

    init {
        Observables
            .combineLatest(
                refreshRelay,
                theaterFilterSetting.asObservable(),
                ageFilterSetting.asObservable().distinctUntilChanged()
            )
            .subscribeOn(Schedulers.io())
            .switchMap { (clearCache, theaterFilter, ageFilter) ->
                getMovieList(clearCache)
                    .map { it ->
                        it
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
                    .map { MovieListUiModel.DoneState(it) as MovieListUiModel }
                    .startWith(MovieListUiModel.LoadingState)
                    .onErrorReturnItem(MovieListUiModel.ErrorState)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _uiModel.value = it }
            .disposeOnCleared()
    }

    fun refresh() {
        refreshRelay.accept(true)
    }

    private fun getMovieList(clearCache: Boolean): Observable<List<Movie>> {
        return repository.getNowList(clearCache)
            .map { it.list.sortedBy(Movie::rank) }
    }
}
