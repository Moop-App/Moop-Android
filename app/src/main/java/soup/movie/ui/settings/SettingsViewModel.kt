package soup.movie.ui.settings

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import soup.movie.BuildConfig
import soup.movie.device.InAppUpdateManager
import soup.movie.settings.impl.TheatersSetting
import soup.movie.settings.impl.ThemeOptionSetting
import soup.movie.theme.ThemeOptionManager
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    themeOptionManager: ThemeOptionManager,
    themeOptionSetting: ThemeOptionSetting,
    theatersSetting: TheatersSetting,
    appUpdateManager: InAppUpdateManager
) : ViewModel() {

    private val _themeUiModel = MutableLiveData<ThemeSettingUiModel>()
    val themeUiModel: LiveData<ThemeSettingUiModel>
        get() = _themeUiModel

    private val _theaterUiModel = MutableLiveData<TheaterSettingUiModel>()
    val theaterUiModel: LiveData<TheaterSettingUiModel>
        get() = _theaterUiModel

    val versionUiModel: LiveData<VersionSettingUiModel> = liveData(Dispatchers.IO) {
        val latestVersionCode = appUpdateManager.getAvailableVersionCode()
        emit(VersionSettingUiModel(
            versionCode = BuildConfig.VERSION_CODE,
            versionName = BuildConfig.VERSION_NAME,
            isLatest = BuildConfig.VERSION_CODE >= latestVersionCode
        ))
    }

    init {
        viewModelScope.launch {
            //TODO: Fix again later. This is so ugly...
            themeOptionSetting.asFlow()
                .map { ThemeSettingUiModel(themeOptionManager.getCurrentOption()) }
                .distinctUntilChanged()
                .collect { _themeUiModel.value = it }
        }
        viewModelScope.launch {
            theatersSetting.asFlow()
                .map { TheaterSettingUiModel(it) }
                .distinctUntilChanged()
                .collect { _theaterUiModel.value = it }
        }
    }
}
