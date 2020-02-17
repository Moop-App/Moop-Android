package soup.movie.home.tab

import androidx.lifecycle.LiveData
import soup.movie.home.HomeContentsUiModel

interface HomeContentsViewModel {

    val isLoading: LiveData<Boolean>
    val isError: LiveData<Boolean>
    val contentsUiModel: LiveData<HomeContentsUiModel>
    fun refresh()
}
