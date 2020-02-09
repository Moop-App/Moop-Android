package soup.movie.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import soup.movie.theme.ThemeOptionManager
import javax.inject.Inject

class ThemeOptionViewModel @Inject constructor(
    private val themeOptionManager: ThemeOptionManager
) : ViewModel() {

    private val _uiModel = MutableLiveData<ThemeOptionUiModel>()
    val uiModel: LiveData<ThemeOptionUiModel>
        get() = _uiModel

    init {
        _uiModel.value = ThemeOptionUiModel(
            themeOptionManager
                .getOptions()
                .map { ThemeOptionItemUiModel(it) }
        )
    }

    fun onItemClick(item: ThemeOptionItemUiModel) {
        themeOptionManager.apply(item.themeOption)
    }
}
