/*
 * Copyright 2022 SOUP
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

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import soup.movie.feature.home.tab.HomeContentsScreen
import soup.movie.model.Movie

@Composable
internal fun HomePlanList(
    viewModel: HomePlanViewModel,
    state: LazyGridState = rememberLazyGridState(),
    onItemClick: (Movie) -> Unit,
    onItemLongClick: (Movie) -> Unit,
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val isError by viewModel.isError.collectAsState()
    val movies by viewModel.movies.collectAsState()
    HomeContentsScreen(
        state = state,
        movies = movies,
        onItemClick = onItemClick,
        onItemLongClick = onItemLongClick,
        isLoading = isLoading,
        isError = isError,
        onErrorClick = {
            viewModel.refresh()
        },
    )
}
