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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import soup.movie.core.designsystem.windowsizeclass.WindowWidthSizeClass
import soup.movie.feature.detail.DetailNavGraph
import soup.movie.feature.detail.DetailViewModel
import soup.movie.feature.home.HomeViewModel
import soup.movie.feature.home.MainScreen
import soup.movie.feature.search.SearchScreen
import soup.movie.feature.search.SearchViewModel

private enum class Screen(val route: String) {
    Main("main"),
    Search("search"),
    Detail("detail"),
}

private fun NavController.navigateToDetail(movieId: String) {
    navigate(route = Screen.Detail.route + "/" + movieId)
}

@Composable
fun MainNavGraph(
    mainViewModel: MainViewModel,
    widthSizeClass: WindowWidthSizeClass,
    onTheaterMapClick: () -> Unit,
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
                onMovieItemClick = {
                    navController.navigateToDetail(movieId = it.id)
                },
            )
        }
        composable(Screen.Search.route) {
            val viewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(
                viewModel = viewModel,
                upPress = { navController.navigateUp() },
                onItemClick = {
                    navController.navigateToDetail(movieId = it.id)
                },
            )
        }
        composable(
            route = Screen.Detail.route + "/{movieId}",
            arguments = listOf(navArgument("movieId") { nullable = false })
        ) {
            val viewModel = hiltViewModel<DetailViewModel>()
            DetailNavGraph(
                viewModel = viewModel,
            )
        }
    }

    val uiEvent by mainViewModel.uiEvent.observeAsState()
    val event = uiEvent?.getContentIfNotHandled()
    if (event != null) {
        when (event) {
            is MainUiEvent.ShowDetailUiEvent -> {
                navController.navigateToDetail(movieId = event.movieId)
            }
        }
    }
}
