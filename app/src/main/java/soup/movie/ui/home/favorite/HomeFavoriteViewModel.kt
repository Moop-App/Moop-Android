package soup.movie.ui.home.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import soup.movie.data.MoopRepository
import soup.movie.data.model.Movie
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
        repository.getFavoriteMovieList()
            .map { it.sortedBy(Movie::openDate) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _contentsUiModel.value = HomeContentsUiModel(it)
            }
            .disposeOnCleared()
    }
}
