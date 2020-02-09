package soup.movie.ui.theater.sort

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import soup.movie.model.Theater
import soup.movie.settings.impl.TheatersSetting
import soup.movie.util.swap
import javax.inject.Inject

class TheaterSortViewModel @Inject constructor(
    private val theatersSetting: TheatersSetting
) : ViewModel() {

    private var listSnapshot = mutableListOf<Theater>()

    private val _uiModel = MutableLiveData<TheaterSortUiModel>()
    val uiModel: LiveData<TheaterSortUiModel>
        get() = _uiModel

    init {
        theatersSetting.asFlow()
            .distinctUntilChanged()
            .onEach { updateTheaters(it) }
            .launchIn(viewModelScope)
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
