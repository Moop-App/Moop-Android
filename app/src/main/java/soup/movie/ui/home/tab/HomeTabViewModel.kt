package soup.movie.ui.home.tab

import androidx.lifecycle.LiveData
import soup.movie.ui.base.BaseViewModel
import soup.movie.ui.home.HomeContentsUiModel

abstract class HomeTabViewModel : BaseViewModel() {

    abstract val contentsUiModel: LiveData<HomeContentsUiModel>
    abstract fun refresh()
}
