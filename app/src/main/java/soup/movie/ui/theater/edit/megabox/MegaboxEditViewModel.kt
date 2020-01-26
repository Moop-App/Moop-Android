package soup.movie.ui.theater.edit.megabox

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import soup.movie.domain.theater.edit.TheaterEditManager
import soup.movie.model.Theater
import soup.movie.ui.base.BaseViewModel
import soup.movie.ui.theater.edit.TheaterEditChildUiModel
import javax.inject.Inject

class MegaboxEditViewModel @Inject constructor(
    private val manager: TheaterEditManager
) : BaseViewModel() {

    private val _uiModel = MutableLiveData<TheaterEditChildUiModel>()
    val uiModel: LiveData<TheaterEditChildUiModel>
        get() = _uiModel

    init {
        viewModelScope.launch {
            combine(
                manager.asMegaboxFlow(),
                manager.asSelectedTheaterListFlow()
            ) { megabox, selectedList ->
                TheaterEditChildUiModel(megabox, selectedList)
            }.collect { _uiModel.value = it }
        }
    }

    fun add(theater: Theater): Boolean {
        return manager.add(theater)
    }

    fun remove(theater: Theater) {
        manager.remove(theater)
    }
}
