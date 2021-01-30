package soup.movie.theater.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import soup.movie.model.Theater
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TheaterEditViewModel @Inject constructor(
    private val manager: TheaterEditManager
) : ViewModel() {

    val contentUiModel = liveData {
        emit(TheaterEditContentUiModel.LoadingState)
        withContext(Dispatchers.IO) {
            try {
                manager.loadAsync()
                emit(TheaterEditContentUiModel.DoneState)
            } catch (t: Throwable) {
                Timber.w(t)
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
