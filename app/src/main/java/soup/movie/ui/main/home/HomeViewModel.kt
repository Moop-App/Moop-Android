package soup.movie.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import soup.movie.domain.main.GetMovieFilterUseCase
import soup.movie.domain.main.GetNowMovieListUseCase
import soup.movie.domain.main.GetPlanMovieListUseCase
import soup.movie.ui.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    getNowMovieList: GetNowMovieListUseCase,
    getPlanMovieList: GetPlanMovieListUseCase,
    getMovieFilter: GetMovieFilterUseCase
) : BaseViewModel() {

    private val _uiModel = MutableLiveData<HomeUiModel>()
    val uiModel: LiveData<HomeUiModel>
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
                //TODO:
                getNowMovieList(clearCache, movieFilter)
                //getPlanMovieList(clearCache, movieFilter)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _uiModel.value = it }
            .disposeOnCleared()
    }

    fun refresh() {
        refreshRelay.accept(true)
    }
}
