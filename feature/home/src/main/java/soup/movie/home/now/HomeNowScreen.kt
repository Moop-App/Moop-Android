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
package soup.movie.home.now

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import soup.movie.home.tab.HomeContentsScreen
import soup.movie.model.Movie

@Composable
internal fun HomeNowScreen(
    viewModel: HomeNowViewModel = viewModel(),
    onItemClick: (Movie) -> Unit
) {
    val isLoading by viewModel.isLoading.observeAsState(false)
    val isError by viewModel.isError.observeAsState(false)
    val movies by viewModel.movies.observeAsState(emptyList())
    HomeContentsScreen(
        movies = movies,
        onItemClick = onItemClick,
        isLoading = isLoading,
        isError = isError,
        onErrorClick = {
            viewModel.refresh()
        }
    )
}