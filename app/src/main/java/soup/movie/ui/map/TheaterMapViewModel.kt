package soup.movie.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import soup.movie.data.MoopRepository
import soup.movie.ui.base.BaseViewModel
import javax.inject.Inject

class TheaterMapViewModel @Inject constructor(
    private val repository: MoopRepository
) : BaseViewModel() {

    private val refreshRelay: PublishRelay<Unit> = PublishRelay.create()

    private val _uiModel = MutableLiveData<TheaterMapUiModel>()
    val uiModel: LiveData<TheaterMapUiModel>
        get() = _uiModel

    init {
        refreshRelay
            .switchMap { getCodeObservable() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_uiModel::setValue)
            .disposeOnCleared()
    }

    private fun getCodeObservable(): Observable<TheaterMapUiModel> {
        return repository.getCodeList()
            .map { it.toTheaterList() }
            .map { TheaterMapUiModel.DoneState(it) as TheaterMapUiModel }
            .startWith(TheaterMapUiModel.LoadingState)
            .onErrorReturnItem(TheaterMapUiModel.ErrorState)
    }

    fun onRefresh() {
        refreshRelay.accept(Unit)
    }
}
