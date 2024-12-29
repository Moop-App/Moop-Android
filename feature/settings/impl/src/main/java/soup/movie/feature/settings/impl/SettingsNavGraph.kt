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
package soup.movie.feature.settings.impl

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import soup.compose.material.motion.animation.materialSharedAxisZIn
import soup.compose.material.motion.animation.materialSharedAxisZOut
import soup.movie.feature.settings.impl.home.SettingsScreen
import soup.movie.feature.settings.impl.home.SettingsViewModel
import soup.movie.feature.settings.impl.theme.ThemeOptionScreen
import soup.movie.feature.settings.impl.theme.ThemeOptionViewModel

private sealed interface SettingsScreen {

    @Serializable
    data object Home : SettingsScreen

    @Serializable
    data object ThemeOption : SettingsScreen
}

@Composable
fun SettingsNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = SettingsScreen.Home,
        enterTransition = { materialSharedAxisZIn(forward = true) },
        exitTransition = { materialSharedAxisZOut(forward = true) },
        popEnterTransition = { materialSharedAxisZIn(forward = false) },
        popExitTransition = { materialSharedAxisZOut(forward = false) },
    ) {
        composable<SettingsScreen.Home> {
            val viewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(
                viewModel = viewModel,
                onThemeEditClick = {
                    navController.navigate(SettingsScreen.ThemeOption)
                },
            )
        }
        composable<SettingsScreen.ThemeOption> {
            val viewModel = hiltViewModel<ThemeOptionViewModel>()
            ThemeOptionScreen(viewModel.items)
        }
    }
}
