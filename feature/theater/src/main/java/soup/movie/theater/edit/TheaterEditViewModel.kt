package soup.movie.theater.edit

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import soup.movie.model.Theater

class TheaterEditViewModel @ViewModelInject constructor(
    private val manager: TheaterEditManager
) : ViewModel() {

    val contentUiModel = liveData {
        emit(TheaterEditContentUiModel.LoadingState)
        withContext(Dispatchers.IO) {
            try {
                manager.loadAsync()
                emit(TheaterEditContentUiModel.DoneState)
            } catch (t: Throwable) {
                emit(TheaterEditContentUiModel.ErrorState)
            }
        }
    }

    val cgvUiModel = manager.asCgvFlow()
        .combine(manager.asSelectedTheaterListFlow()) { cgv, selectedList ->
            TheaterEditChildUiModel(cgv, selectedList)
        }
        .asLiveData()

    val lotteUiModel = manager.asLotteFlow()
        .combine(manager.asSelectedTheaterListFlow()) { lotte, selectedList ->
            TheaterEditChildUiModel(lotte, selectedList)
        }
        .asLiveData()

    val megaboxUiModel = manager.asMegaboxFlow()
        .combine(manager.asSelectedTheaterListFlow()) { megabox, selectedList ->
            TheaterEditChildUiModel(megabox, selectedList)
        }
        .asLiveData()

    val footerUiModel = manager.asSelectedTheaterListFlow()
        .map { TheaterEditFooterUiModel(it) }
        .asLiveData()

    fun add(theater: Theater): Boolean {
        return manager.add(theater)
    }

    fun remove(theater: Theater) {
        manager.remove(theater)
    }

    fun onConfirmClicked() {
        manager.save()
    }
}
