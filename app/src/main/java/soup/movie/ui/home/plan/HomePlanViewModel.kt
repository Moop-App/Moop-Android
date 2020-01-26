package soup.movie.ui.home.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import soup.movie.data.repository.MoopRepository
import soup.movie.domain.home.GetMovieFilterUseCase
import soup.movie.domain.home.GetPlanMovieListUseCase
import soup.movie.ui.home.HomeContentsUiModel
import soup.movie.ui.home.tab.HomeContentsTabViewModel
import javax.inject.Inject

class HomePlanViewModel @Inject constructor(
    getPlanMovieList: GetPlanMovieListUseCase,
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
        viewModelScope.launch {
            getMovieFilter()
                .flowOn(Dispatchers.IO)
                .flatMapLatest { getPlanMovieList(it) }
                .flowOn(Dispatchers.Main)
                .collect {
                    _contentsUiModel.value = HomeContentsUiModel(it.movies)
                }
        }

        updateList()
    }

    override fun refresh() {
        updateList()
    }

    private fun updateList() {
        if (_isLoading.value == true) {
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
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
    }
}
