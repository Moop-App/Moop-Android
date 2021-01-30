package soup.movie.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import soup.movie.ext.postValueIfNew
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _headerUiModel = MutableLiveData<HomeHeaderUiModel>()
    val headerUiModel: LiveData<HomeHeaderUiModel>
        get() = _headerUiModel

    fun onNowTabClick() {
        _headerUiModel.postValueIfNew(HomeHeaderUiModel.Now)
    }

    fun onPlanTabClick() {
        _headerUiModel.postValueIfNew(HomeHeaderUiModel.Plan)
    }

    fun onFavoriteTabClick() {
        _headerUiModel.postValueIfNew(HomeHeaderUiModel.Favorite)
    }
}
