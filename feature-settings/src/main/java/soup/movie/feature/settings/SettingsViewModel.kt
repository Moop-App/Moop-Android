/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.feature.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import soup.movie.data.settings.AppSettings
import soup.movie.feature.common.install.InAppUpdateManager
import soup.movie.feature.theme.ThemeOptionManager
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    themeOptionManager: ThemeOptionManager,
    appSettings: AppSettings,
    appUpdateManager: InAppUpdateManager
) : ViewModel() {

    // TODO: Fix again later. This is so ugly...
    val themeUiModel = appSettings.getThemeOptionFlow()
        .map { ThemeSettingUiModel(themeOptionManager.getCurrentOption()) }
        .distinctUntilChanged()
        .asLiveData()

    val theaterUiModel: LiveData<TheaterSettingUiModel> = appSettings.getFavoriteTheaterListFlow()
        .map { TheaterSettingUiModel(it) }
        .distinctUntilChanged()
        .asLiveData()

    private val _versionUiModel = MutableLiveData<VersionSettingUiModel>()
    val versionUiModel: LiveData<VersionSettingUiModel>
        get() = _versionUiModel

    var showVersionUpdateDialog by mutableStateOf(false)

    init {
        viewModelScope.launch {
            val latestVersionCode = appUpdateManager.getAvailableVersionCode()
            val isLatest = BuildConfig.VERSION_CODE >= latestVersionCode
            _versionUiModel.value = VersionSettingUiModel(
                versionCode = BuildConfig.VERSION_CODE,
                versionName = BuildConfig.VERSION_NAME,
                isLatest = BuildConfig.VERSION_CODE >= latestVersionCode
            )
            showVersionUpdateDialog = isLatest.not()
        }
    }
}
