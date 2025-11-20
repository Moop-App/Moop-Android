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
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import soup.movie.feature.detail.rememberDetailComposableFactory
import soup.movie.feature.home.rememberHomeComposableFactory
import soup.movie.feature.search.rememberSearchComposableFactory
import soup.movie.feature.settings.rememberSettingsComposableFactory

private sealed interface Screen : NavKey {

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
fun MainNavGraph() {
    // Create navigation state with Main as the start route
    val navigationState = rememberNavigationState(
        startRoute = Screen.Main,
    )

    val navigator = remember { Navigator(navigationState) }

    // Define entry provider for all destinations
    val entryProvider = entryProvider<NavKey> {
        entry<Screen.Main> {
            val factory = rememberHomeComposableFactory()
            factory.HomeNavGraph(
                onSearchClick = {
                    navigator.navigate(Screen.Search)
                },
                onSettingsClick = {
                    navigator.navigate(Screen.Settings)
                },
                onMovieItemClick = {
                    navigator.navigate(Screen.Detail(movieId = it.id))
                },
            )
        }
        entry<Screen.Search> {
            val factory = rememberSearchComposableFactory()
            factory.SearchScreen(
                upPress = { navigator.goBack() },
                onItemClick = {
                    navigator.navigate(Screen.Detail(movieId = it.id))
                },
            )
        }
        entry<Screen.Settings> {
            val factory = rememberSettingsComposableFactory()
            factory.SettingsNavGraph()
        }
        entry<Screen.Detail> { key ->
            val factory = rememberDetailComposableFactory()
            factory.DetailNavGraph(movieId = key.movieId)
        }
    }

    // Replace NavHost with NavDisplay
    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() },
    )
}
