package soup.movie.ui.theater.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import soup.movie.domain.theater.edit.TheaterEditManager
import soup.movie.model.Theater
import javax.inject.Inject

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
                emit(TheaterEditContentUiModel.ErrorState)
            }
        }
    }

    val footerUiModel = manager.asSelectedTheaterListFlow()
        .map { TheaterEditFooterUiModel(it) }
        .asLiveData()

    fun onConfirmClicked() {
        manager.save()
    }

    fun remove(theater: Theater) {
        manager.remove(theater)
    }
}
