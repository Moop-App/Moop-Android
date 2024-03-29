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
package soup.movie.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import soup.movie.core.analytics.EventAnalytics
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val analytics: EventAnalytics,
) : ViewModel() {

    private val _selectedMainTab = MutableStateFlow(MainTabUiModel.Home)
    val selectedMainTab: StateFlow<MainTabUiModel> = _selectedMainTab

    private val _selectedHomeTab = MutableStateFlow(HomeTabUiModel.Now)
    val selectedHomeTab: StateFlow<HomeTabUiModel> = _selectedHomeTab

    fun onMainTabSelected(mainTab: MainTabUiModel) {
        viewModelScope.launch {
            _selectedMainTab.emit(mainTab)
        }
    }

    fun onHomeTabSelected(homeTab: HomeTabUiModel) {
        viewModelScope.launch {
            _selectedHomeTab.emit(homeTab)
        }
    }

    fun onMovieClick() {
        analytics.clickMovie()
    }

    fun onFilterButtonClick() {
        analytics.clickMenuFilter()
    }
}
