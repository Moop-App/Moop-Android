package soup.movie.ui.theater.edit.cgv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import soup.movie.domain.theater.edit.TheaterEditManager
import soup.movie.model.Theater
import soup.movie.ui.base.BaseViewModel
import soup.movie.ui.theater.edit.TheaterEditChildUiModel
import javax.inject.Inject

class CgvEditViewModel @Inject constructor(
    private val manager: TheaterEditManager
) : BaseViewModel() {

    private val _uiModel = MutableLiveData<TheaterEditChildUiModel>()
    val uiModel: LiveData<TheaterEditChildUiModel>
        get() = _uiModel

    init {
        manager.asCgvFlow()
            .combine(manager.asSelectedTheaterListFlow()) { cgv, selectedList ->
                TheaterEditChildUiModel(cgv, selectedList)
            }
            .onEach { _uiModel.value = it }
            .launchIn(viewModelScope)
    }

    fun add(theater: Theater): Boolean {
        return manager.add(theater)
    }

    fun remove(theater: Theater) {
        manager.remove(theater)
    }
}
