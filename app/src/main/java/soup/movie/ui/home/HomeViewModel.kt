package soup.movie.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import soup.movie.ui.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(
) : BaseViewModel() {

    private enum class Tab {
        Now, Plan
    }

    private val _headerUiModel = MutableLiveData<HomeHeaderUiModel>()
    val headerUiModel: LiveData<HomeHeaderUiModel>
        get() = _headerUiModel

    private val currentTabRelay = BehaviorRelay.createDefault(Tab.Now)

    init {
        currentTabRelay
            .distinctUntilChanged()
            .subscribe {
                _headerUiModel.postValue(HomeHeaderUiModel(isNow = it == Tab.Now))
            }
            .disposeOnCleared()
    }

    fun onNowTabClick() {
        currentTabRelay.accept(Tab.Now)
    }

    fun onPlanTabClick() {
        currentTabRelay.accept(Tab.Plan)
    }
}
