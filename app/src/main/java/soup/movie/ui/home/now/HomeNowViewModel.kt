package soup.movie.ui.home.now

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import soup.movie.domain.home.GetMovieFilterUseCase
import soup.movie.domain.home.GetNowMovieListUseCase
import soup.movie.ui.base.BaseViewModel
import soup.movie.ui.home.HomeContentsUiModel
import soup.movie.ui.home.HomeUiMapper
import javax.inject.Inject

class HomeNowViewModel @Inject constructor(
    getNowMovieList: GetNowMovieListUseCase,
    getMovieFilter: GetMovieFilterUseCase
) : BaseViewModel(), HomeUiMapper {

    private var doRefreshRelay = BehaviorRelay.createDefault(false)

    private val _contentsUiModel = MutableLiveData<HomeContentsUiModel>()
    val contentsUiModel: LiveData<HomeContentsUiModel>
        get() = _contentsUiModel

    init {
        getMovieFilter()
            .subscribeOn(Schedulers.io())
            .switchMap { movieFilter ->
                onRefreshed { byUser ->
                    getNowMovieList(movieFilter, clearCache = byUser)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _contentsUiModel.value = it.toContentsUiModel() }
            .disposeOnCleared()
    }

    fun refresh() {
        doRefreshRelay.accept(true)
    }

    private inline fun <T> onRefreshed(
        crossinline action: (byUser: Boolean) -> Observable<T>
    ): Observable<T> {
        return BehaviorRelay.createDefault(false)
            .also { doRefreshRelay = it }
            .switchMap { action(it) }
    }
}
