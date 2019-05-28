package soup.movie.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import soup.movie.data.MoopRepository
import soup.movie.data.helper.VersionHelper.currentVersion
import soup.movie.settings.impl.TheatersSetting
import soup.movie.settings.impl.ThemeOptionSetting
import soup.movie.ui.BaseViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
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

        themeOptionSetting.asObservable()
            .map { ThemeSettingUiModel(it) }
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

        repository.getVersion()
            .startWith(currentVersion())
            .defaultIfEmpty(currentVersion())
            .onErrorReturnItem(currentVersion())
            .distinctUntilChanged()
            .map { VersionSettingUiModel(currentVersion(), it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _versionUiModel.value = it }
            .disposeOnCleared()
    }
}
