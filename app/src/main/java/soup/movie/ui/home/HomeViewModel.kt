package soup.movie.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
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

    private val requestRelay = BehaviorRelay.createDefault(Request(isNow = true, refresh = false))

    private val _headerUiModel = MutableLiveData<HomeHeaderUiModel>()
    val headerUiModel: LiveData<HomeHeaderUiModel>
        get() = _headerUiModel

    private val _contentsUiModel = MutableLiveData<HomeContentsUiModel>()
    val contentsUiModel: LiveData<HomeContentsUiModel>
        get() = _contentsUiModel

    init {
        requestRelay
            .delay(100, TimeUnit.MILLISECONDS)
            .doOnNext { _headerUiModel.postValue(it.toHeaderUiModel()) }
            .switchMap { request ->
                getMovieFilter()
                    .switchMap { movieFilter ->
                        if (request.isNow) {
                            getNowMovieList(request.refresh, movieFilter)
                        } else {
                            getPlanMovieList(request.refresh, movieFilter)
                        }
                    }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _contentsUiModel.value = it.toContentsUiModel() }
            .disposeOnCleared()
    }

    fun onNowClick() {
        requestRelay.accept(Request(isNow = true, refresh = false))
    }

    fun onPlanClick() {
        requestRelay.accept(Request(isNow = false, refresh = false))
    }

    fun refresh() {
        val wasNow = requestRelay.value?.isNow ?: true
        requestRelay.accept(Request(isNow = wasNow, refresh = true))
    }
}
