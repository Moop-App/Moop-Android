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
package soup.movie.feature.home.plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import soup.movie.data.repository.MovieRepository
import soup.movie.data.settings.AppSettings
import soup.movie.domain.movie.getDDay
import soup.movie.feature.home.domain.getMovieFilterFlow
import soup.movie.model.Movie
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomePlanViewModel @Inject constructor(
    private val appSettings: AppSettings,
    private val repository: MovieRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>>
        get() = _movies

    init {
        viewModelScope.launch {
            updateList()
            repository.getPlanMovieList()
                .combine(appSettings.getMovieFilterFlow()) { movieList, movieFilter ->
                    movieList.asSequence()
                        .sortedBy { it.getDDay() }
                        .filter { movieFilter(it) }
                        .toList()
                }
                .onStart { delay(200) }
                .collect {
                    _movies.emit(it)
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            updateList()
        }
    }

    private suspend fun updateList() {
        if (_isLoading.value) {
            return
        }
        _isLoading.emit(true)
        try {
            repository.updatePlanMovieList()
            _isLoading.emit(false)
            _isError.emit(false)
        } catch (t: Throwable) {
            Timber.w(t)
            _isLoading.emit(false)
            _isError.emit(true)
        }
    }
}
