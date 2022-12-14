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
package soup.movie.feature.home.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import soup.movie.common.IoDispatcher
import soup.movie.data.repository.MovieRepository
import soup.movie.model.Movie
import javax.inject.Inject

@HiltViewModel
class HomeFavoriteViewModel @Inject constructor(
    repository: MovieRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    init {
        repository.getFavoriteMovieList()
            .onEach {
                val favoriteMovieList = it.sortedBy(Movie::openDate)
                _movies.emit(favoriteMovieList)
            }
            .flowOn(ioDispatcher)
            .launchIn(viewModelScope)
    }
}
