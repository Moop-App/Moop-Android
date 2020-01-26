package soup.movie.ui.home.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import soup.movie.data.repository.MoopRepository
import soup.movie.domain.home.GetMovieFilterUseCase
import soup.movie.domain.model.getDDay
import soup.movie.model.Movie
import soup.movie.ui.home.HomeContentsUiModel
import soup.movie.ui.home.tab.HomeContentsTabViewModel
import timber.log.Timber
import javax.inject.Inject

class HomePlanViewModel @Inject constructor(
    getMovieFilter: GetMovieFilterUseCase,
    private val repository: MoopRepository
) : HomeContentsTabViewModel() {

    private val _isLoading = MutableLiveData<Boolean>(false)
    override val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isError = MutableLiveData<Boolean>(false)
    override val isError: LiveData<Boolean>
        get() = _isError

    private val _contentsUiModel = MutableLiveData<HomeContentsUiModel>()
    override val contentsUiModel: LiveData<HomeContentsUiModel>
        get() = _contentsUiModel

    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateList()
            repository.getPlanMovieList()
                .combine(getMovieFilter()) { movieList, movieFilter ->
                    movieList.asSequence()
                        .sortedBy(Movie::getDDay)
                        .filter { movieFilter(it) }
                        .toList()
                }
                .collect {
                    Timber.d("QQQQ init: ${it.size}")
                    _contentsUiModel.postValue(HomeContentsUiModel(it))
                }
        }
    }

    override fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            updateList()
        }
    }

    private suspend fun updateList() {
        if (_isLoading.value == true) {
            return
        }
        _isLoading.postValue(true)
        try {
            repository.updatePlanMovieList()
            _isLoading.postValue(false)
            _isError.postValue(false)
        } catch (t: Throwable) {
            _isLoading.postValue(false)
            _isError.postValue(true)
        }
    }
}
