package soup.movie.ui.main.now

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import soup.movie.data.MoopRepository
import soup.movie.data.model.AgeFilter
import soup.movie.data.model.GenreFilter
import soup.movie.data.model.Movie
import soup.movie.data.model.TheaterFilter
import soup.movie.domain.filter.FilterGroup
import soup.movie.domain.filter.GetFilterGroupUseCase
import soup.movie.ui.BaseViewModel
import soup.movie.ui.main.movie.MovieListUiModel
import javax.inject.Inject

class NowViewModel @Inject constructor(
    getFilterGroup: GetFilterGroupUseCase,
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
                getFilterGroup.asObservable()
            )
            .subscribeOn(Schedulers.io())
            .switchMap { (clearCache, filterGroup) -> getMovieList(clearCache, filterGroup) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _uiModel.value = it }
            .disposeOnCleared()
    }

    fun refresh() {
        refreshRelay.accept(true)
    }

    private fun getMovieList(
        clearCache: Boolean,
        filterGroup: FilterGroup
    ): Observable<MovieListUiModel> {
        return repository.getNowList(clearCache)
            .map { it.list.sortedBy(Movie::rank) }
            .map { it ->
                it.asSequence()
                    .filter { it.isFilterBy(filterGroup.theaterFilter) }
                    .filter { it.isFilterBy(filterGroup.ageFilter) }
                    .filter { it.isFilterBy(filterGroup.genreFilter) }
                    .toList()
            }
            .map { MovieListUiModel.DoneState(it) as MovieListUiModel }
            .startWith(MovieListUiModel.LoadingState)
            .onErrorReturnItem(MovieListUiModel.ErrorState)
    }

    private fun Movie.isFilterBy(theaterFilter: TheaterFilter): Boolean {
        return (theaterFilter.hasCgv() and isScreeningAtCgv()) or
            (theaterFilter.hasLotteCinema() and isScreeningAtLotteCinema()) or
            (theaterFilter.hasMegabox() and isScreeningAtMegabox())
    }

    private fun Movie.isFilterBy(ageFilter: AgeFilter): Boolean {
        return (ageFilter.hasAll() and isScreeningForAgeAll()) or
            (ageFilter.has12() and isScreeningOverAge12()) or
            (ageFilter.has15() and isScreeningOverAge15()) or
            (ageFilter.has19() and isScreeningOverAge19())
    }

    private fun Movie.isFilterBy(genreFilter: GenreFilter): Boolean {
        return genre?.any { it !in genreFilter.blacklist }
            ?: genre.isNullOrEmpty() and genreFilter.isEtcIncluded()
    }
}
