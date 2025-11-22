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
package soup.movie.feature.detail.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

sealed interface DetailScreen : NavKey {

    @Serializable
    data class Home(val movieId: String) : DetailScreen

    @Serializable
    data class Poster(val posterUrl: String) : DetailScreen
}

@Composable
fun DetailNavGraph(movieId: String) {
    // Create navigation state with Detail.Home as the start route
    val navigationState = rememberDetailNavigationState(
        startRoute = DetailScreen.Home(movieId),
    )

    val navigator = remember { DetailNavigator(navigationState) }

    // Define entry provider for detail destinations
    val entryProvider = entryProvider<NavKey> {
        entry<DetailScreen.Home> { key ->
            val viewModel = hiltViewModel<DetailViewModel, DetailViewModel.Factory>(
                creationCallback = { factory ->
                    factory.create(key)
                }
            )
            DetailScreen(
                viewModel = viewModel,
                onPosterClick = {
                    navigator.navigate(DetailScreen.Poster(posterUrl = it))
                },
            )
        }
        entry<DetailScreen.Poster> { key ->
            DetailPoster(
                posterUrl = key.posterUrl,
            )
        }
    }

    // Replace NavHost with NavDisplay
    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() },
    )
}
