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
package soup.movie.feature.home.tab

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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import soup.movie.core.designsystem.icon.MovieIcons
import soup.movie.domain.movie.getDDayLabel
import soup.movie.domain.movie.isBest
import soup.movie.domain.movie.isDDay
import soup.movie.domain.movie.isNew
import soup.movie.feature.home.favorite.MovieAgeBadge
import soup.movie.feature.home.favorite.MovieBestTag
import soup.movie.feature.home.favorite.MovieDDayTag
import soup.movie.feature.home.favorite.MovieNewTag
import soup.movie.feature.home.filter.rippleTheme
import soup.movie.model.MovieModel
import soup.movie.resources.R

@Composable
fun MovieList(
    movies: List<MovieModel>,
    onItemClick: (MovieModel) -> Unit,
    onLongItemClick: (MovieModel) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    state: LazyGridState = rememberLazyGridState()
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 105.dp),
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
    ) {
        items(
            movies,
            key = { it.id },
            contentType = { "movie" },
        ) { movie ->
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
    movie: MovieModel,
    onClick: (MovieModel) -> Unit,
    onLongClick: (MovieModel) -> Unit,
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
                AsyncImage(
                    movie.posterUrl,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .aspectRatio(27 / 40f)
                        .combinedClickable(
                            onClick = { onClick(movie) },
                            onLongClick = { onLongClick(movie) }
                        ),
                    contentScale = ContentScale.Crop
                )
                MovieAgeBadge(
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
            MovieIcons.ViewModule,
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onBackground),
            modifier = Modifier.size(72.dp)
        )
        Text(
            text = stringResource(R.string.no_movies_description),
            color = MaterialTheme.colors.onBackground
        )
    }
}
