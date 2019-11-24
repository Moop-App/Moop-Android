package soup.movie.ui.home.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import soup.movie.domain.home.GetMovieFilterUseCase
import soup.movie.domain.home.GetPlanMovieListUseCase
import soup.movie.ui.home.HomeContentsUiModel
import soup.movie.ui.home.HomeUiMapper
import soup.movie.ui.home.tab.HomeTabViewModel
import javax.inject.Inject

class HomePlanViewModel @Inject constructor(
    getPlanMovieList: GetPlanMovieListUseCase,
    getMovieFilter: GetMovieFilterUseCase
) : HomeTabViewModel(), HomeUiMapper {

    private var doRefreshRelay = BehaviorRelay.createDefault(false)

    private val _contentsUiModel = MutableLiveData<HomeContentsUiModel>()
    override val contentsUiModel: LiveData<HomeContentsUiModel>
        get() = _contentsUiModel

    init {
        getMovieFilter()
            .subscribeOn(Schedulers.io())
            .switchMap { movieFilter ->
                onRefreshed { byUser ->
                    getPlanMovieList(movieFilter, clearCache = byUser)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _contentsUiModel.value = it.toContentsUiModel() }
            .disposeOnCleared()
    }

    override fun refresh() {
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
