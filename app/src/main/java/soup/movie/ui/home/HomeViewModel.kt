package soup.movie.ui.home

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

    private val nowRelay = BehaviorRelay.createDefault(true)
    private val refreshRelay = BehaviorRelay.createDefault(false)

    private val _uiModel = MutableLiveData<HomeUiModel>()
    val uiModel: LiveData<HomeUiModel>
        get() = _uiModel

    init {
        Observables
            .combineLatest(
                nowRelay.distinctUntilChanged(),
                refreshRelay,
                getMovieFilter()
            )
            .subscribeOn(Schedulers.io())
            .switchMap { (isNow, clearCache, movieFilter) ->
                if (isNow) {
                    getNowMovieList(clearCache, movieFilter)
                } else {
                    getPlanMovieList(clearCache, movieFilter)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _uiModel.value = it }
            .disposeOnCleared()
    }

    fun onNowClick() {
        nowRelay.accept(true)
    }

    fun onPlanClick() {
        nowRelay.accept(false)
    }

    fun refresh() {
        refreshRelay.accept(true)
    }
}
