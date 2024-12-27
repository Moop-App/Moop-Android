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
package soup.movie.feature.home.impl.tab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import soup.movie.model.MovieModel

@Composable
fun HomeContentsScreen(
    movies: List<MovieModel>,
    onItemClick: (MovieModel) -> Unit,
    onItemLongClick: (MovieModel) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    isLoading: Boolean = false,
    isError: Boolean = false,
    onErrorClick: () -> Unit = {},
) {
    Box(modifier = modifier.fillMaxSize()) {
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
        if (isError) {
            CommonError(
                onClick = onErrorClick,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
        AnimatedVisibility(
            visible = isLoading,
            modifier = Modifier.align(Alignment.TopCenter),
            enter = fadeIn(
                animationSpec = tween(durationMillis = 400),
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(delayMillis = 500, durationMillis = 150),
            ),
        ) {
            ContentLoadingProgressBar(
                modifier = Modifier
                    .padding(top = 60.dp)
                    .padding(all = 12.dp)
                    .size(size = 48.dp),
            )
        }
    }
}
