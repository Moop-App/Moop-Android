package soup.movie.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import soup.movie.ui.base.BaseViewModel
import soup.movie.util.postValueIfNew
import javax.inject.Inject

class HomeViewModel @Inject constructor(
) : BaseViewModel() {

    private val _headerUiModel = MutableLiveData<HomeHeaderUiModel>()
    val headerUiModel: LiveData<HomeHeaderUiModel>
        get() = _headerUiModel

    fun onNowTabClick() {
        _headerUiModel.postValueIfNew(HomeHeaderUiModel(isNow = true))
    }

    fun onPlanTabClick() {
        _headerUiModel.postValueIfNew(HomeHeaderUiModel(isNow = false))
    }
}
