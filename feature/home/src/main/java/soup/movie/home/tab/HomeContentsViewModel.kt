package soup.movie.home.tab

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import soup.movie.home.HomeContentsUiModel

abstract class HomeContentsViewModel : ViewModel() {

    abstract val isLoading: LiveData<Boolean>
    abstract val isError: LiveData<Boolean>
    abstract val contentsUiModel: LiveData<HomeContentsUiModel>
    abstract fun refresh()
}
