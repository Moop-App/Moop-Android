package soup.movie.theater.sort

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import soup.movie.ext.swap
import soup.movie.model.Theater
import soup.movie.settings.AppSettings

class TheaterSortViewModel @ViewModelInject constructor(
        private val appSettings: AppSettings
) : ViewModel() {

    private var listSnapshot = mutableListOf<Theater>()

    private val _uiModel = MutableLiveData<TheaterSortUiModel>()
    val uiModel: LiveData<TheaterSortUiModel>
        get() = _uiModel

    init {
        appSettings.getFavoriteTheaterListFlow()
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
        appSettings.favoriteTheaterList = listSnapshot.toList()
    }
}
