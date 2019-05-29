package soup.movie.ui.main.now

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import soup.movie.domain.main.GetMovieFilterUseCase
import soup.movie.domain.main.GetNowMovieListUseCase
import soup.movie.ui.BaseViewModel
import soup.movie.ui.main.movie.MovieListUiModel
import javax.inject.Inject

class NowViewModel @Inject constructor(
    getNowMovieList: GetNowMovieListUseCase,
    getMovieFilter: GetMovieFilterUseCase
) : BaseViewModel() {

    private val _uiModel = MutableLiveData<MovieListUiModel>()
    val uiModel: LiveData<MovieListUiModel>
        get() = _uiModel

    private val refreshRelay = BehaviorRelay.createDefault(false)

    init {
        Observables
            .combineLatest(
                refreshRelay,
                getMovieFilter()
            )
            .subscribeOn(Schedulers.io())
            .switchMap { (clearCache, movieFilter) ->
                getNowMovieList(clearCache, movieFilter)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _uiModel.value = it }
            .disposeOnCleared()
    }

    fun refresh() {
        refreshRelay.accept(true)
    }
}
