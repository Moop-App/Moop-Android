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
package soup.movie.feature.theater.sort

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import soup.movie.data.settings.AppSettings
import soup.movie.model.TheaterModel
import javax.inject.Inject

@HiltViewModel
class TheaterSortViewModel @Inject constructor(
    private val appSettings: AppSettings,
) : ViewModel() {

    private var listSnapshot = mutableListOf<TheaterModel>()

    var selectedTheaters by mutableStateOf<List<TheaterModel>>(emptyList())
        private set

    init {
        appSettings.getFavoriteTheaterListFlow()
            .distinctUntilChanged()
            .onEach { updateTheaters(it) }
            .launchIn(viewModelScope)
    }

    private fun updateTheaters(it: List<TheaterModel>) {
        listSnapshot = it.toMutableList()
        selectedTheaters = it
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        listSnapshot.swap(fromPosition, toPosition)
        updateTheaters(listSnapshot)
    }

    suspend fun saveSnapshot() {
        appSettings.setFavoriteTheaterList(listSnapshot.toList())
    }
}
