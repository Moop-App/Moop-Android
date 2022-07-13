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
package soup.movie.ui.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import soup.movie.home.HomeViewModel
import soup.movie.home.MainScreen
import soup.movie.model.Movie
import soup.movie.search.SearchScreen
import soup.movie.search.SearchViewModel
import soup.movie.ui.windowsizeclass.WindowWidthSizeClass

private enum class Screen(val route: String) {
    Main("main"),
    Search("search"),
}

@Composable
fun MainNavGraph(
    widthSizeClass: WindowWidthSizeClass,
    onTheaterMapClick: () -> Unit,
    onMovieItemClick: (Movie) -> Unit,
) {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = Screen.Main.route,
    ) {
        composable(Screen.Main.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
            MainScreen(
                widthSizeClass = widthSizeClass,
                viewModel = viewModel,
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onTheaterMapClick = onTheaterMapClick,
                onMovieItemClick = onMovieItemClick,
            )
        }
        composable(Screen.Search.route) {
            val viewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(
                viewModel = viewModel,
                upPress = { navController.navigateUp() },
                onItemClick = onMovieItemClick,
            )
        }
    }
}
