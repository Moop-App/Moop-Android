package soup.movie.ui.theater.sort

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import soup.movie.data.model.Theater
import soup.movie.settings.impl.TheatersSetting
import soup.movie.ui.base.BaseViewModel
import soup.movie.util.swap
import javax.inject.Inject

class TheaterSortViewModel @Inject constructor(
    private val theatersSetting: TheatersSetting
) : BaseViewModel() {

    private val theatersObservable = BehaviorRelay.create<List<Theater>>()

    private var listSnapshot = mutableListOf<Theater>()

    private val _uiModel = MutableLiveData<TheaterSortUiModel>()
    val uiModel: LiveData<TheaterSortUiModel>
        get() = _uiModel

    init {
        theatersObservable
            .distinctUntilChanged()
            .doOnNext { listSnapshot = it.toMutableList() }
            .map { TheaterSortUiModel(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _uiModel.value = it }
            .disposeOnCleared()

        theatersSetting.asObservable()
            .distinctUntilChanged()
            .subscribe { theatersObservable.accept(it) }
            .disposeOnCleared()
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        listSnapshot.swap(fromPosition, toPosition)
    }

    fun saveSnapshot() {
        theatersSetting.set(listSnapshot.toList())
    }
}
