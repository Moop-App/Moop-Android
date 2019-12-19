package soup.movie.ui.theater.sort

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import soup.movie.data.model.Theater
import soup.movie.settings.impl.TheatersSetting
import soup.movie.ui.base.BaseViewModel
import soup.movie.util.swap
import javax.inject.Inject

class TheaterSortViewModel @Inject constructor(
    private val theatersSetting: TheatersSetting
) : BaseViewModel() {

    private var listSnapshot = mutableListOf<Theater>()

    private val _uiModel = MutableLiveData<TheaterSortUiModel>()
    val uiModel: LiveData<TheaterSortUiModel>
        get() = _uiModel

    init {
        theatersSetting.asObservable()
            .distinctUntilChanged()
            .subscribe { updateTheaters(it) }
            .disposeOnCleared()
    }

    private fun updateTheaters(it: List<Theater>) {
        listSnapshot = it.toMutableList()
        _uiModel.value = TheaterSortUiModel(it)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        listSnapshot.swap(fromPosition, toPosition)
    }

    fun saveSnapshot() {
        theatersSetting.set(listSnapshot.toList())
    }
}
