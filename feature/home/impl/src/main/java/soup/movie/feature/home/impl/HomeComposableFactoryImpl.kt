/*
 * Copyright 2024 SOUP
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
package soup.movie.feature.home.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import soup.movie.feature.home.HomeComposableFactory
import soup.movie.model.MovieModel
import javax.inject.Inject

class HomeComposableFactoryImpl @Inject constructor() : HomeComposableFactory {

    @Composable
    override fun HomeNavGraph(
        onSearchClick: () -> Unit,
        onSettingsClick: () -> Unit,
        onMovieItemClick: (MovieModel) -> Unit,
    ) {
        HomeNavGraph(
            viewModel = hiltViewModel(),
            onSearchClick = onSearchClick,
            onSettingsClick = onSettingsClick,
            onMovieItemClick = onMovieItemClick,
        )
    }

    @Composable
    override fun MovieList(
        movies: List<MovieModel>,
        onItemClick: (MovieModel) -> Unit,
        onLongItemClick: (MovieModel) -> Unit,
        modifier: Modifier,
    ) {
        soup.movie.feature.home.impl.tab.MovieList(
            movies = movies,
            onItemClick = onItemClick,
            onLongItemClick = onLongItemClick,
            modifier = modifier,
        )
    }

    @Composable
    override fun NoMovieItems(
        modifier: Modifier,
    ) {
        soup.movie.feature.home.impl.tab.NoMovieItems(
            modifier = modifier,
        )
    }

    @Composable
    override fun MovieAgeTag(
        age: Int,
        modifier: Modifier,
    ) {
        soup.movie.feature.home.impl.favorite.MovieAgeTag(
            age = age,
            modifier = modifier,
        )
    }

    @Composable
    override fun MovieDDayTag(
        text: String,
        modifier: Modifier,
    ) {
        soup.movie.feature.home.impl.favorite.MovieDDayTag(
            text = text,
            modifier = modifier,
        )
    }
}
