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
package soup.movie.home.filter

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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import soup.movie.model.repository.MovieRepository
import soup.movie.settings.AppSettings
import soup.movie.settings.model.AgeFilter
import soup.movie.settings.model.AgeFilter.Companion.FLAG_AGE_12
import soup.movie.settings.model.AgeFilter.Companion.FLAG_AGE_15
import soup.movie.settings.model.AgeFilter.Companion.FLAG_AGE_19
import soup.movie.settings.model.AgeFilter.Companion.FLAG_AGE_ALL
import soup.movie.settings.model.GenreFilter
import soup.movie.settings.model.TheaterFilter
import soup.movie.settings.model.TheaterFilter.Companion.FLAG_THEATER_CGV
import soup.movie.settings.model.TheaterFilter.Companion.FLAG_THEATER_LOTTE
import soup.movie.settings.model.TheaterFilter.Companion.FLAG_THEATER_MEGABOX
import javax.inject.Inject

@HiltViewModel
class HomeFilterViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val appSettings: AppSettings
) : ViewModel() {

    private var theaterFilter: TheaterFilter? = null
    private var lastGenreFilter: GenreFilter? = null

    private val _theaterUiModel = MutableLiveData<TheaterFilterUiModel>()
    val theaterUiModel: LiveData<TheaterFilterUiModel>
        get() = _theaterUiModel

    val ageUiModel = appSettings.getAgeFilterFlow()
        .distinctUntilChanged()
        .map { it.toUiModel() }
        .asLiveData()

    var genreFilterList by mutableStateOf<List<GenreFilterItem>>(emptyList())
        private set

    init {
        appSettings.getTheaterFilterFlow()
            .distinctUntilChanged()
            .onEach {
                theaterFilter = it
                _theaterUiModel.value = it.toUiModel()
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            val allGenre = getGenreList()
            appSettings.getGenreFilterFlow()
                .collect { filter ->
                    lastGenreFilter = filter
                    genreFilterList = allGenre.map {
                        GenreFilterItem(
                            name = it,
                            isChecked = filter.blacklist.contains(it).not()
                        )
                    }
                }
        }
    }

    private suspend fun getGenreList(): List<String> {
        return mutableListOf<String>().apply {
            addAll(repository.getGenreList())
            add(GenreFilter.GENRE_ETC)
        }
    }

    private fun TheaterFilter.toUiModel(): TheaterFilterUiModel {
        return TheaterFilterUiModel(
            hasCgv = hasCgv(),
            hasLotteCinema = hasLotteCinema(),
            hasMegabox = hasMegabox()
        )
    }

    private fun AgeFilter.toUiModel(): AgeFilterUiModel {
        return AgeFilterUiModel(
            hasAll = hasAll(),
            has12 = has12(),
            has15 = has15(),
            has19 = has19()
        )
    }

    fun onCgvFilterChanged(isChecked: Boolean) {
        updateTheaterFilter(isChecked, FLAG_THEATER_CGV)
    }

    fun onLotteFilterChanged(isChecked: Boolean) {
        updateTheaterFilter(isChecked, FLAG_THEATER_LOTTE)
    }

    fun onMegaboxFilterChanged(isChecked: Boolean) {
        updateTheaterFilter(isChecked, FLAG_THEATER_MEGABOX)
    }

    private fun updateTheaterFilter(isChecked: Boolean, flag: Int) {
        theaterFilter?.let {
            val success = if (isChecked) {
                it.addFlag(flag)
            } else {
                it.removeFlag(flag)
            }
            if (success) {
                viewModelScope.launch {
                    appSettings.setTheaterFilter(it)
                }
            }
        }
    }

    fun onAgeAllFilterClicked() {
        updateAgeFilter(FLAG_AGE_ALL)
    }

    fun onAge12FilterClicked() {
        updateAgeFilter(FLAG_AGE_ALL or FLAG_AGE_12)
    }

    fun onAge15FilterClicked() {
        updateAgeFilter(FLAG_AGE_ALL or FLAG_AGE_12 or FLAG_AGE_15)
    }

    fun onAge19FilterClicked() {
        updateAgeFilter(FLAG_AGE_ALL or FLAG_AGE_12 or FLAG_AGE_15 or FLAG_AGE_19)
    }

    private fun updateAgeFilter(flags: Int) {
        viewModelScope.launch {
            appSettings.setAgeFilter(AgeFilter(flags))
        }
    }

    fun onGenreFilterClick(genre: String, isChecked: Boolean) {
        val lastGenreSet = lastGenreFilter?.blacklist?.toMutableSet() ?: return
        val changed = if (isChecked) {
            lastGenreSet.remove(genre)
        } else {
            lastGenreSet.add(genre)
        }
        if (changed) {
            viewModelScope.launch {
                appSettings.setGenreFilter(GenreFilter(lastGenreSet))
            }
        }
    }
}
