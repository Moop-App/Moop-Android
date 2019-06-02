package soup.movie.ui.theater.edit.lotte

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import soup.movie.data.model.Theater
import soup.movie.domain.theater.edit.TheaterEditManager
import soup.movie.ui.BaseViewModel
import soup.movie.ui.theater.edit.TheaterEditChildUiModel
import javax.inject.Inject

class LotteEditViewModel @Inject constructor(
    private val manager: TheaterEditManager
) : BaseViewModel() {

    private val _uiModel = MutableLiveData<TheaterEditChildUiModel>()
    val uiModel: LiveData<TheaterEditChildUiModel>
        get() = _uiModel

    init {
        Observables
            .combineLatest(
                manager.asLotteObservable().map { it.list },
                manager.asSelectedTheatersSubject(),
                ::TheaterEditChildUiModel
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _uiModel.value = it }
            .disposeOnCleared()
    }

    fun add(theater: Theater): Boolean {
        return manager.add(theater)
    }

    fun remove(theater: Theater) {
        manager.remove(theater)
    }
}
