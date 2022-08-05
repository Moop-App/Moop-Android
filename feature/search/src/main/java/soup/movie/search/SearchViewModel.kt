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
package soup.movie.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import soup.movie.feature.common.analytics.EventAnalytics
import soup.movie.data.repository.MovieRepository
import soup.movie.feature.common.ext.setValueIfNew
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val analytics: EventAnalytics,
) : ViewModel() {

    private val _query = MutableLiveData("")
    val query: LiveData<String>
        get() = _query

    @OptIn(FlowPreview::class)
    val uiModel: LiveData<SearchContentsUiModel> = _query.asFlow()
        .debounce(300)
        .map { it.trim() }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            val movies = repository.searchMovie(query)
            flowOf(
                SearchContentsUiModel(
                    movies = movies,
                    hasNoItem = query.isNotEmpty() && movies.isEmpty()
                )
            )
        }
        .asLiveData()

    fun onQueryChanged(query: String) {
        _query.setValueIfNew(query)
    }

    fun onMovieClick() {
        analytics.clickMovie()
    }
}
