package soup.movie.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import soup.movie.model.repository.MoopRepository

class SearchViewModel @AssistedInject constructor(
    private val repository: MoopRepository
) : ViewModel() {

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

    @AssistedInject.Factory
    interface Factory {
        fun create(): SearchViewModel
    }
}
