package soup.movie.ui.home.now

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import soup.movie.data.repository.MoopRepository
import soup.movie.domain.home.GetMovieFilterUseCase
import soup.movie.domain.home.GetNowMovieListUseCase
import soup.movie.ui.home.HomeContentsUiModel
import soup.movie.ui.home.tab.HomeContentsTabViewModel
import javax.inject.Inject

class HomeNowViewModel @Inject constructor(
    getNowMovieList: GetNowMovieListUseCase,
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
        getMovieFilter()
            .subscribeOn(Schedulers.io())
            .switchMap { getNowMovieList(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _contentsUiModel.value = HomeContentsUiModel(it.movies)
            }
            .disposeOnCleared()

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
                    repository.updateNowList()
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
