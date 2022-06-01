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
package soup.movie.home.tab

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import soup.movie.ext.getDDayLabel
import soup.movie.ext.isBest
import soup.movie.ext.isDDay
import soup.movie.ext.isNew
import soup.movie.home.R
import soup.movie.home.favorite.MovieAgeTag
import soup.movie.home.favorite.MovieBestTag
import soup.movie.home.favorite.MovieDDayTag
import soup.movie.home.favorite.MovieNewTag
import soup.movie.home.filter.rippleTheme
import soup.movie.model.Movie

@Composable
fun MovieList(
    movies: List<Movie>,
    onItemClick: (Movie) -> Unit,
    onLongItemClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState()
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(integerResource(R.integer.grid_span_count)),
        modifier = modifier,
        state = state,
        contentPadding = PaddingValues(8.dp)
    ) {
        items(movies) { movie ->
            MovieItem(
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
private fun MovieItem(
    movie: Movie,
    onClick: (Movie) -> Unit,
    onLongClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalRippleTheme provides rippleTheme(Color.White)) {
        Surface(
            modifier = modifier,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
            shape = MaterialTheme.shapes.medium,
            elevation = 0.dp
        ) {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(movie.posterUrl),
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
fun NoMovieItems(
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
