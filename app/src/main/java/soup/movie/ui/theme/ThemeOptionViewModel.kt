package soup.movie.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import soup.movie.theme.ThemeOptionManager
import soup.movie.ui.BaseViewModel
import javax.inject.Inject

class ThemeOptionViewModel @Inject constructor(
    private val themeOptionManager: ThemeOptionManager
) : BaseViewModel() {

    private val _uiModel = MutableLiveData<ThemeOptionUiModel>()
    val uiModel: LiveData<ThemeOptionUiModel>
        get() = _uiModel

    init {
        val options = themeOptionManager.createOptions()
        _uiModel.value = ThemeOptionUiModel(
            options.map {
                ThemeOptionItemUiModel(it)
            }
        )
    }

    fun onItemClick(item: ThemeOptionItemUiModel) {
        themeOptionManager.apply(item.themeOption)
    }
}
