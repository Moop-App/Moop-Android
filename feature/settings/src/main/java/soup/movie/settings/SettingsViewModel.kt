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
package soup.movie.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import soup.movie.install.InAppUpdateManager
import soup.movie.theme.ThemeOptionManager
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
}
