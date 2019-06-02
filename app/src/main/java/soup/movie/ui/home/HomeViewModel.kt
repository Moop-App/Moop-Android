package soup.movie.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import soup.movie.domain.home.GetMovieFilterUseCase
import soup.movie.domain.home.GetNowMovieListUseCase
import soup.movie.domain.home.GetPlanMovieListUseCase
import soup.movie.ui.base.BaseViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    getNowMovieList: GetNowMovieListUseCase,
    getPlanMovieList: GetPlanMovieListUseCase,
    getMovieFilter: GetMovieFilterUseCase
) : BaseViewModel(), HomeUiMapper {

    private val nowRelay = BehaviorRelay.createDefault(true)
    private val refreshRelay = BehaviorRelay.createDefault(Unit)

    private val _headerUiModel = MutableLiveData<HomeHeaderUiModel>()
    val headerUiModel: LiveData<HomeHeaderUiModel>
        get() = _headerUiModel

    private val _contentsUiModel = MutableLiveData<HomeContentsUiModel>()
    val contentsUiModel: LiveData<HomeContentsUiModel>
        get() = _contentsUiModel

    init {
        nowRelay
            .distinctUntilChanged()
            .delay(100, TimeUnit.MILLISECONDS)
            .doOnNext { _headerUiModel.postValue(it.toHeaderUiModel()) }
            .switchMap { isNow ->
                Observables
                    .combineLatest(
                        refreshRelay,
                        getMovieFilter()
                    )
                    .switchMap { (_, movieFilter) ->
                        if (isNow) {
                            getNowMovieList(false, movieFilter)
                        } else {
                            getPlanMovieList(false, movieFilter)
                        }
                    }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _contentsUiModel.value = it.toContentsUiModel() }
            .disposeOnCleared()
    }

    fun onNowClick() {
        nowRelay.accept(true)
    }

    fun onPlanClick() {
        nowRelay.accept(false)
    }

    fun refresh() {
        refreshRelay.accept(Unit)
    }
}
