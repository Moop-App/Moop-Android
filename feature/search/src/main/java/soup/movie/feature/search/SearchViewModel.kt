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
package soup.movie.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import soup.movie.core.analytics.EventAnalytics
import soup.movie.data.repository.MovieRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val analytics: EventAnalytics,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    @OptIn(FlowPreview::class)
    val uiModel: StateFlow<SearchUiModel> = _query
        .debounce(300)
        .map { it.trim() }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            val movies = repository.searchMovie(query)
            flowOf(
                SearchUiModel.Success(
                    movies = movies,
                    hasNoItem = query.isNotEmpty() && movies.isEmpty()
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = SearchUiModel.None,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    fun onQueryChanged(query: String) {
        viewModelScope.launch {
            _query.emit(query)
        }
    }

    fun onMovieClick() {
        analytics.clickMovie()
    }
}
