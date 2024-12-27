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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import soup.compose.material.motion.animation.materialSharedAxisZIn
import soup.compose.material.motion.animation.materialSharedAxisZOut

private sealed interface DetailScreen {

    @Serializable
    data class Home(val movieId: String) : DetailScreen

    @Serializable
    data class Poster(val posterUrl: String) : DetailScreen
}

@Composable
fun DetailNavGraph(movieId: String) {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = DetailScreen.Home(movieId),
        enterTransition = { materialSharedAxisZIn(forward = true) },
        exitTransition = { materialSharedAxisZOut(forward = true) },
        popEnterTransition = { materialSharedAxisZIn(forward = false) },
        popExitTransition = { materialSharedAxisZOut(forward = false) },
    ) {
        composable<DetailScreen.Home> {
            val viewModel = hiltViewModel<DetailViewModel>()
            DetailScreen(
                viewModel = viewModel,
                onPosterClick = {
                    navController.navigate(DetailScreen.Poster(posterUrl = it))
                },
            )
        }
        composable<DetailScreen.Poster> { backStackEntry ->
            val posterUrl = backStackEntry.toRoute<DetailScreen.Poster>().posterUrl
            DetailPoster(
                posterUrl = posterUrl,
            )
        }
    }
}
