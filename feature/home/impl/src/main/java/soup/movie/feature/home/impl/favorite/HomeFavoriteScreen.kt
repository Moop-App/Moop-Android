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
package soup.movie.feature.home.impl.favorite

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.feature.home.impl.tab.MovieList
import soup.movie.feature.home.impl.tab.NoMovieItems
import soup.movie.model.MovieModel
import soup.movie.resources.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFavoriteScreen(
    viewModel: HomeFavoriteViewModel,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    onSettingsClick: () -> Unit,
    onItemClick: (MovieModel) -> Unit,
    onItemLongClick: (MovieModel) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val isTopAtCurrentTab by remember {
        derivedStateOf {
            state.firstVisibleItemIndex == 0 && state.firstVisibleItemScrollOffset == 0
        }
    }
    BackHandler(enabled = isTopAtCurrentTab.not()) {
        if (isTopAtCurrentTab.not()) {
            coroutineScope.launch {
                state.animateScrollToItem(0)
            }
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.menu_favorite))
                },
                actions = {
                    IconButton(onClick = { onSettingsClick() }) {
                        Icon(
                            MovieIcons.Settings,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            val movies by viewModel.movies.collectAsState()
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
}
