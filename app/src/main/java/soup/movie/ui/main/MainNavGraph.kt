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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import soup.movie.core.designsystem.windowsizeclass.WindowWidthSizeClass
import soup.movie.feature.detail.rememberDetailComposableFactory
import soup.movie.feature.home.rememberHomeComposableFactory
import soup.movie.feature.search.rememberSearchComposableFactory
import soup.movie.feature.settings.rememberSettingsComposableFactory

private sealed interface Screen {

    @Serializable
    data object Main : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data class Detail(val movieId: String) : Screen
}

@Composable
fun MainNavGraph(
    widthSizeClass: WindowWidthSizeClass,
) {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = Screen.Main,
    ) {
        composable<Screen.Main> {
            val factory = rememberHomeComposableFactory()
            factory.HomeNavGraph(
                widthSizeClass = widthSizeClass,
                onSearchClick = {
                    navController.navigate(Screen.Search)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings)
                },
                onMovieItemClick = {
                    navController.navigate(Screen.Detail(movieId = it.id))
                },
            )
        }
        composable<Screen.Search> {
            val factory = rememberSearchComposableFactory()
            factory.SearchScreen(
                upPress = { navController.navigateUp() },
                onItemClick = {
                    navController.navigate(Screen.Detail(movieId = it.id))
                },
            )
        }
        composable<Screen.Settings> {
            val factory = rememberSettingsComposableFactory()
            factory.SettingsNavGraph()
        }
        composable<Screen.Detail> { backStackEntry ->
            val movieId = backStackEntry.toRoute<Screen.Detail>().movieId
            val factory = rememberDetailComposableFactory()
            factory.DetailNavGraph(movieId = movieId)
        }
    }
}
