package soup.movie.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import soup.movie.data.MoopRepository
import soup.movie.ui.base.BaseViewModel
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val repository: MoopRepository
) : BaseViewModel() {

    private val _uiModel = MutableLiveData<SearchContentsUiModel>()
    val uiModel: LiveData<SearchContentsUiModel>
        get() = _uiModel

    fun searchFor(query: String) {
        viewModelScope.launch {
            val movies = withContext(Dispatchers.IO) {
                repository.searchMovie(query)
            }
            _uiModel.value = SearchContentsUiModel(
                movies = movies,
                hasNoItem = movies.isEmpty()
            )
        }
    }
}
