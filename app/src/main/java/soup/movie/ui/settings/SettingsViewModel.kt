package soup.movie.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import soup.movie.BuildConfig
import soup.movie.device.InAppUpdateManager
import soup.movie.settings.impl.TheatersSetting
import soup.movie.settings.impl.ThemeOptionSetting
import soup.movie.theme.ThemeOptionManager
import soup.movie.ui.base.BaseViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    themeOptionManager: ThemeOptionManager,
    themeOptionSetting: ThemeOptionSetting,
    theatersSetting: TheatersSetting,
    appUpdateManager: InAppUpdateManager
) : BaseViewModel() {

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
        //TODO: Fix again later. This is so ugly...
        themeOptionSetting.asObservable()
            .map { ThemeSettingUiModel(themeOptionManager.getCurrentOption()) }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _themeUiModel.value = it }
            .disposeOnCleared()

        theatersSetting.asObservable()
            .map { TheaterSettingUiModel(it) }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _theaterUiModel.value = it }
            .disposeOnCleared()
    }
}
