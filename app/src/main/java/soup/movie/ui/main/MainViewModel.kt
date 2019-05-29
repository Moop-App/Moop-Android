package soup.movie.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import soup.movie.data.MoopRepository
import soup.movie.data.model.MovieId
import soup.movie.settings.impl.LastMainTabSetting
import soup.movie.ui.BaseViewModel
import soup.movie.ui.EventLiveData
import soup.movie.ui.MutableEventLiveData
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val lastMainTabSetting: LastMainTabSetting,
    private val repository: MoopRepository
) : BaseViewModel() {

    private val _uiModel = MutableLiveData<MainUiModel>()
    val uiModel: LiveData<MainUiModel>
        get() = _uiModel

    private val _uiEvent = MutableEventLiveData<MainUiEvent>()
    val uiEvent: EventLiveData<MainUiEvent>
        get() = _uiEvent

    init {
        lastMainTabSetting
            .asObservable()
            .distinctUntilChanged()
            .map { mapToViewState(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _uiModel.value = it }
            .disposeOnCleared()
    }

    private fun mapToViewState(tabMode: LastMainTabSetting.Tab): MainUiModel =
        when (tabMode) {
            LastMainTabSetting.Tab.Now -> MainUiModel.NowState
            LastMainTabSetting.Tab.Plan -> MainUiModel.PlanState
        }

    fun setCurrentTab(mode: LastMainTabSetting.Tab) {
        Timber.d("setCurrentTab: %s", mode)
        lastMainTabSetting.set(mode)
    }

    fun requestMovie(movieId: MovieId?) {
        if (movieId != null) {
            repository.getMovie(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .map { ShowDetailUiEvent(it) }
                .subscribe { _uiEvent.event = it }
                .disposeOnCleared()
        }
    }
}
