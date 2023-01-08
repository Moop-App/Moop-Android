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
package soup.movie.feature.theatermap

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import soup.movie.data.repository.TheaterRepository
import soup.movie.feature.theatermap.internal.CgvMarkerUiModel
import soup.movie.feature.theatermap.internal.LotteCinemaMarkerUiModel
import soup.movie.feature.theatermap.internal.MegaboxMarkerUiModel
import soup.movie.feature.theatermap.internal.TheaterMarkerUiModel
import soup.movie.model.TheaterAreaGroupModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TheaterMapViewModel @Inject constructor(
    private val repository: TheaterRepository
) : ViewModel() {

    var uiModel by mutableStateOf<List<TheaterMarkerUiModel>>(emptyList())
        private set

    var selectedTheater by mutableStateOf<TheaterMarkerUiModel?>(null)
        private set

    init {
        viewModelScope.launch {
            uiModel = loadUiModel()
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            if (uiModel.isEmpty()) {
                uiModel = loadUiModel()
            }
        }
    }

    fun onTheaterSelected(theater: TheaterMarkerUiModel) {
        selectedTheater = theater
    }

    fun onTheaterUnselected() {
        selectedTheater = null
    }

    private suspend fun loadUiModel(): List<TheaterMarkerUiModel> {
        return try {
            repository.getCodeList().toTheaterList()
        } catch (t: Throwable) {
            Timber.w(t)
            emptyList()
        }
    }

    private fun TheaterAreaGroupModel.toTheaterList(): List<TheaterMarkerUiModel> {
        return cgv.flatMap { group ->
            group.theaterList.map {
                CgvMarkerUiModel(
                    areaCode = group.area.code,
                    code = it.code,
                    name = "CGV ${it.name}",
                    lat = it.lat,
                    lng = it.lng
                )
            }
        } + lotte.flatMap { group ->
            group.theaterList.map {
                LotteCinemaMarkerUiModel(
                    areaCode = group.area.code,
                    code = it.code,
                    name = "롯데시네마 ${it.name}",
                    lat = it.lat,
                    lng = it.lng
                )
            }
        } + megabox.flatMap { group ->
            group.theaterList.map {
                MegaboxMarkerUiModel(
                    areaCode = group.area.code,
                    code = it.code,
                    name = "메가박스 ${it.name}",
                    lat = it.lat,
                    lng = it.lng
                )
            }
        }
    }
}
