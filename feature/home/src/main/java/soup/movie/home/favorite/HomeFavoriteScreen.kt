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

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import soup.movie.ext.getDDayLabel
import soup.movie.ext.isBest
import soup.movie.ext.isDDay
import soup.movie.ext.isNew
import soup.movie.ext.showToast
import soup.movie.home.R
import soup.movie.home.filter.rippleTheme
import soup.movie.model.Movie

@Composable
internal fun HomeFavoriteScreen(
    movies: List<Movie>,
    onItemClick: (Movie) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    BackHandler(enabled = listState.isTop().not()) {
        coroutineScope.launch {
            listState.firstVisibleItemIndex
            if (listState.isScrollInProgress) {
                listState.stopScroll()
                listState.scrollToItem(0)
            } else {
                listState.animateScrollToItem(0)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (movies.isEmpty()) {
            NoMovieItems(modifier = Modifier.align(Alignment.Center))
        } else {
            val context = LocalContext.current
            FavoriteMovieList(
                movies,
                onItemClick = onItemClick,
                onLongItemClick = {
                    context.showToast(it.title)
                },
                state = listState
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoriteMovieList(
    movies: List<Movie>,
    onItemClick: (Movie) -> Unit,
    onLongItemClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(integerResource(R.integer.grid_span_count)),
        modifier = modifier,
        state = state,
        contentPadding = PaddingValues(8.dp)
    ) {
        items(movies) { movie ->
            FavoriteMovieItem(
                movie = movie,
                onClick = onItemClick,
                onLongClick = onLongItemClick,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoriteMovieItem(
    movie: Movie,
    onClick: (Movie) -> Unit,
    onLongClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalRippleTheme provides rippleTheme(Color(0xFFBDBDBD))) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            elevation = 0.dp
        ) {
            Box {
                Image(
                    painter = rememberImagePainter(movie.posterUrl) {
                        placeholder(R.drawable.bg_on_surface_dim)
                        crossfade(true)
                    },
                    contentDescription = movie.title,
                    modifier = Modifier
                        .aspectRatio(27 / 40f)
                        .combinedClickable(
                            onClick = { onClick(movie) },
                            onLongClick = { onLongClick(movie) }
                        ),
                    contentScale = ContentScale.Crop
                )
                MovieAgeTag(
                    age = movie.age,
                    modifier = Modifier
                        .padding(7.dp)
                        .align(Alignment.BottomStart)
                )
                when {
                    movie.isDDay() -> MovieDDayTag(
                        text = movie.getDDayLabel().orEmpty(),
                        modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.BottomEnd)
                    )
                    movie.isBest() -> MovieBestTag(
                        modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.BottomEnd)
                    )
                    movie.isNew() -> MovieNewTag(
                        modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.BottomEnd)
                    )
                }
            }
        }
    }
}

@Composable
private fun NoMovieItems(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(R.drawable.ic_round_no_movies),
            contentDescription = null,
            modifier = Modifier.size(72.dp)
        )
        Text(
            text = stringResource(R.string.no_movies_description),
            color = MaterialTheme.colors.onBackground
        )
    }
}

private fun LazyListState.isTop(): Boolean {
    return firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0
}
