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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import soup.movie.core.designsystem.theme.MovieTheme
import soup.movie.core.imageloading.AsyncImage
import soup.movie.domain.movie.getDDayLabel
import soup.movie.domain.movie.isDDay
import soup.movie.feature.home.impl.favorite.MovieAgeBadge
import soup.movie.feature.home.impl.favorite.MovieDDayTag
import soup.movie.model.MovieModel
import soup.movie.model.TheaterRatingsModel

@Composable
fun MovieList(
    movies: List<MovieModel>,
    onItemClick: (MovieModel) -> Unit,
    onLongItemClick: (MovieModel) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    state: LazyGridState = rememberLazyGridState(),
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
                modifier = Modifier.padding(4.dp),
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
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MovieTheme.shapes.small,
    ) {
        Box {
            AsyncImage(
                movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .aspectRatio(27 / 40f)
                    .combinedClickable(
                        onClick = { onClick(movie) },
                        onLongClick = { onLongClick(movie) },
                    ),
                contentScale = ContentScale.Crop,
            )
            MovieAgeBadge(
                age = movie.age,
                modifier = Modifier
                    .padding(7.dp)
                    .align(Alignment.BottomStart),
            )
            if (movie.isDDay()) {
                MovieDDayTag(
                    text = movie.getDDayLabel().orEmpty(),
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.BottomEnd),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun MovieListPreview(
    @PreviewParameter(MovieListPreviewParameterProvider::class) movies: List<MovieModel>,
) {
    MovieTheme {
        Surface {
            MovieList(
                movies = movies,
                onItemClick = {},
                onLongItemClick = {},
            )
        }
    }
}

private class MovieListPreviewParameterProvider : PreviewParameterProvider<List<MovieModel>> {
    override val values: Sequence<List<MovieModel>> = sequenceOf(
        listOf(-1, 0, 11, 12, 14, 15, 18, 19, 20).mapIndexed { index, age ->
            MovieModel(
                id = index.toString(),
                score = index,
                title = "Movie Title",
                posterUrl = "",
                openDate = "2024.12.31",
                isNow = false,
                age = age,
                nationFilter = null,
                genres = null,
                boxOffice = 0,
                theater = TheaterRatingsModel(
                    cgv = null,
                    lotte = null,
                    megabox = null,
                ),
            )
        },
    )
}
