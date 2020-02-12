package soup.movie.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import soup.movie.install.InAppUpdateManager
import soup.movie.theme.ThemeOptionManager

class SettingsViewModel @AssistedInject constructor(
    themeOptionManager: ThemeOptionManager,
    appSettings: AppSettings,
    appUpdateManager: InAppUpdateManager
) : ViewModel() {

    //TODO: Fix again later. This is so ugly...
    val themeUiModel = appSettings.getThemeOptionFlow()
        .map { ThemeSettingUiModel(themeOptionManager.getCurrentOption()) }
        .distinctUntilChanged()
        .asLiveData()

    val theaterUiModel: LiveData<TheaterSettingUiModel> = appSettings.getFavoriteTheaterListFlow()
        .map { TheaterSettingUiModel(it) }
        .distinctUntilChanged()
        .asLiveData()

    val versionUiModel: LiveData<VersionSettingUiModel> = liveData(Dispatchers.IO) {
        val latestVersionCode = appUpdateManager.getAvailableVersionCode()
        emit(
            VersionSettingUiModel(
                versionCode = BuildConfig.VERSION_CODE,
                versionName = BuildConfig.VERSION_NAME,
                isLatest = BuildConfig.VERSION_CODE >= latestVersionCode
            )
        )
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(): SettingsViewModel
    }
}
