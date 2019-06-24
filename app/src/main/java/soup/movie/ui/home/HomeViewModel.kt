package soup.movie.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import soup.movie.domain.home.GetMovieFilterUseCase
import soup.movie.domain.home.GetNowMovieListUseCase
import soup.movie.domain.home.GetPlanMovieListUseCase
import soup.movie.ui.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    getNowMovieList: GetNowMovieListUseCase,
    getPlanMovieList: GetPlanMovieListUseCase,
    getMovieFilter: GetMovieFilterUseCase
) : BaseViewModel(), HomeUiMapper {

    private enum class Tab {
        Now, Plan
    }

    private val currentTabRelay = BehaviorRelay.createDefault(Tab.Now)
    private var doRefreshRelay = BehaviorRelay.createDefault(false)

    private val _headerUiModel = MutableLiveData<HomeHeaderUiModel>()
    val headerUiModel: LiveData<HomeHeaderUiModel>
        get() = _headerUiModel

    private val _contentsUiModel = MutableLiveData<HomeContentsUiModel>()
    val contentsUiModel: LiveData<HomeContentsUiModel>
        get() = _contentsUiModel

    init {
        Observables
            .combineLatest(isNow(), getMovieFilter())
            .subscribeOn(Schedulers.io())
            .switchMap { (isNow, movieFilter) ->
                onRefreshed { byUser ->
                    if (isNow) {
                        getNowMovieList(movieFilter, clearCache = byUser)
                    } else {
                        getPlanMovieList(movieFilter, clearCache = byUser)
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _contentsUiModel.value = it.toContentsUiModel() }
            .disposeOnCleared()
    }

    fun onNowClick() {
        currentTabRelay.accept(Tab.Now)
    }

    fun onPlanClick() {
        currentTabRelay.accept(Tab.Plan)
    }

    fun refresh() {
        doRefreshRelay.accept(true)
    }

    private fun isNow(): Observable<Boolean> {
        return currentTabRelay
            .distinctUntilChanged()
            .map { it == Tab.Now }
            .doOnNext {
                _headerUiModel.postValue(it.toHeaderUiModel())
            }
    }

    private inline fun <T> onRefreshed(
        crossinline action: (byUser: Boolean) -> Observable<T>
    ): Observable<T> {
        return BehaviorRelay.createDefault(false)
            .also { doRefreshRelay = it }
            .switchMap { action(it) }
    }
}
