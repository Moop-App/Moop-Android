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
package soup.movie.feature.home.now

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import soup.movie.data.repository.MovieRepository
import soup.movie.data.settings.AppSettings
import soup.movie.feature.home.domain.getMovieFilterFlow
import soup.movie.model.Movie
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeNowViewModel @Inject constructor(
    private val appSettings: AppSettings,
    private val repository: MovieRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean>
        get() = _isError

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = _movies

    init {
        viewModelScope.launch {
            updateList()
            repository.getNowMovieList()
                .combine(appSettings.getMovieFilterFlow()) { movieList, movieFilter ->
                    movieList.asSequence()
                        .sortedBy(Movie::score)
                        .filter { movieFilter(it) }
                        .toList()
                }
                .onStart { delay(200) }
                .collect {
                    _movies.postValue(it)
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            updateList()
        }
    }

    private suspend fun updateList() {
        if (_isLoading.value == true) {
            return
        }
        _isLoading.postValue(true)
        try {
            repository.updateNowMovieList()
            _isLoading.postValue(false)
            _isError.postValue(false)
        } catch (t: Throwable) {
            Timber.w(t)
            _isLoading.postValue(false)
            _isError.postValue(true)
        }
    }
}
