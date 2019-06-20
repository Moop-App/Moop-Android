package soup.movie.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import soup.movie.BuildConfig
import soup.movie.data.MoopRepository
import soup.movie.data.model.Version
import soup.movie.settings.impl.TheatersSetting
import soup.movie.settings.impl.ThemeOptionSetting
import soup.movie.theme.ThemeOptionManager
import soup.movie.ui.base.BaseViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    themeOptionManager: ThemeOptionManager,
    themeOptionSetting: ThemeOptionSetting,
    theatersSetting: TheatersSetting,
    repository: MoopRepository
) : BaseViewModel() {

    private val _themeUiModel = MutableLiveData<ThemeSettingUiModel>()
    val themeUiModel: LiveData<ThemeSettingUiModel>
        get() = _themeUiModel

    private val _theaterUiModel = MutableLiveData<TheaterSettingUiModel>()
    val theaterUiModel: LiveData<TheaterSettingUiModel>
        get() = _theaterUiModel

    private val _versionUiModel = MutableLiveData<VersionSettingUiModel>()
    val versionUiModel: LiveData<VersionSettingUiModel>
        get() = _versionUiModel

    init {
        repository
            .refreshVersion()
            .subscribe()
            .disposeOnCleared()

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

        val current = currentVersion()
        repository.getVersion()
            .startWith(current)
            .defaultIfEmpty(current)
            .onErrorReturnItem(current)
            .distinctUntilChanged()
            .map { latest ->
                VersionSettingUiModel(
                    current,
                    latest,
                    isLatest = current.versionCode >= latest.versionCode
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _versionUiModel.value = it }
            .disposeOnCleared()
    }

    private fun currentVersion(): Version {
        return Version(
            BuildConfig.VERSION_CODE,
            BuildConfig.VERSION_NAME
        )
    }
}
