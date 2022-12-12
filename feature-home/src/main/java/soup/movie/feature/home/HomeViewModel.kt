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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import soup.movie.core.analytics.EventAnalytics
import soup.movie.feature.common.ext.setValueIfNew
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val analytics: EventAnalytics,
) : ViewModel() {

    private val _selectedMainTab = MutableLiveData(MainTabUiModel.Home)
    val selectedMainTab: LiveData<MainTabUiModel>
        get() = _selectedMainTab

    private val _selectedHomeTab = MutableLiveData(HomeTabUiModel.Now)
    val selectedHomeTab: LiveData<HomeTabUiModel>
        get() = _selectedHomeTab

    fun onMainTabSelected(mainTab: MainTabUiModel) {
        _selectedMainTab.setValueIfNew(mainTab)
    }

    fun onHomeTabSelected(homeTab: HomeTabUiModel) {
        _selectedHomeTab.setValueIfNew(homeTab)
    }

    fun onMovieClick() {
        analytics.clickMovie()
    }

    fun onFilterButtonClick() {
        analytics.clickMenuFilter()
    }
}
