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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import soup.movie.core.designsystem.windowsizeclass.WindowWidthSizeClass
import soup.movie.feature.detail.rememberDetailComposableFactory
import soup.movie.feature.home.rememberHomeComposableFactory
import soup.movie.feature.search.rememberSearchComposableFactory
import soup.movie.feature.settings.rememberSettingsComposableFactory

private enum class Screen(val route: String) {
    Main("main"),
    Search("search"),
    Settings("settings"),
    Detail("detail"),
}

private fun NavController.navigateToDetail(movieId: String) {
    navigate(route = Screen.Detail.route + "/" + movieId)
}

@Composable
fun MainNavGraph(
    widthSizeClass: WindowWidthSizeClass,
) {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = Screen.Main.route,
    ) {
        composable(Screen.Main.route) {
            val factory = rememberHomeComposableFactory()
            factory.HomeNavGraph(
                widthSizeClass = widthSizeClass,
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onMovieItemClick = {
                    navController.navigateToDetail(movieId = it.id)
                },
            )
        }
        composable(Screen.Search.route) {
            val factory = rememberSearchComposableFactory()
            factory.SearchScreen(
                upPress = { navController.navigateUp() },
                onItemClick = {
                    navController.navigateToDetail(movieId = it.id)
                },
            )
        }
        composable(Screen.Settings.route) {
            val factory = rememberSettingsComposableFactory()
            factory.SettingsNavGraph()
        }
        composable(
            route = Screen.Detail.route + "/{movieId}",
            arguments = listOf(navArgument("movieId") { nullable = false }),
        ) {
            val factory = rememberDetailComposableFactory()
            factory.DetailNavGraph()
        }
    }
}
