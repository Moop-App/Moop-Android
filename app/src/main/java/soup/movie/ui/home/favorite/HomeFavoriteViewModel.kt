package soup.movie.ui.home.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import soup.movie.model.Movie
import soup.movie.model.repository.MoopRepository
import soup.movie.ui.base.BaseViewModel
import soup.movie.ui.home.HomeContentsUiModel
import javax.inject.Inject

class HomeFavoriteViewModel @Inject constructor(
    repository: MoopRepository
) : BaseViewModel() {

    private val _contentsUiModel = MutableLiveData<HomeContentsUiModel>()
    val contentsUiModel: LiveData<HomeContentsUiModel>
        get() = _contentsUiModel

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavoriteMovieList().collect {
                val favoriteMovieList = it.sortedBy(Movie::openDate)
                _contentsUiModel.postValue(HomeContentsUiModel(favoriteMovieList))
            }
        }
    }
}
