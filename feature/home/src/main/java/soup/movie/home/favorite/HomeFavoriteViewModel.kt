package soup.movie.home.favorite

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import soup.movie.home.HomeContentsUiModel
import soup.movie.model.Movie
import soup.movie.model.repository.MoopRepository

class HomeFavoriteViewModel @ViewModelInject constructor(
    repository: MoopRepository
) : ViewModel() {

    private val _contentsUiModel = MutableLiveData<HomeContentsUiModel>()
    val contentsUiModel: LiveData<HomeContentsUiModel>
        get() = _contentsUiModel

    init {
        repository.getFavoriteMovieList()
            .onEach {
                val favoriteMovieList = it.sortedBy(Movie::openDate)
                _contentsUiModel.postValue(HomeContentsUiModel(favoriteMovieList))
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }
}
