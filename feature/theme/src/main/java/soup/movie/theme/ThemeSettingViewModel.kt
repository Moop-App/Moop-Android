package soup.movie.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThemeSettingViewModel @Inject constructor(
    private val themeOptionManager: ThemeOptionManager
) : ViewModel() {

    private val _uiModel = MutableLiveData<ThemeSettingUiModel>()
    val uiModel: LiveData<ThemeSettingUiModel>
        get() = _uiModel

    init {
        _uiModel.value = ThemeSettingUiModel(
            themeOptionManager
                .getOptions()
                .map { ThemeSettingItemUiModel(it) }
        )
    }

    fun onItemClick(item: ThemeSettingItemUiModel) {
        themeOptionManager.apply(item.themeOption)
    }
}
