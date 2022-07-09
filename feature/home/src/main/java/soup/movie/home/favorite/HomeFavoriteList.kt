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
package soup.movie.home.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import soup.movie.home.tab.MovieList
import soup.movie.home.tab.NoMovieItems
import soup.movie.model.Movie

@Composable
internal fun HomeFavoriteList(
    viewModel: HomeFavoriteViewModel,
    state: LazyGridState = rememberLazyGridState(),
    onItemClick: (Movie) -> Unit,
    onItemLongClick: (Movie) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val movies by viewModel.movies.observeAsState(emptyList())
        if (movies.isEmpty()) {
            NoMovieItems(modifier = Modifier.align(Alignment.Center))
        } else {
            MovieList(
                state = state,
                movies = movies,
                onItemClick = onItemClick,
                onLongItemClick = onItemLongClick,
            )
        }
    }
}
