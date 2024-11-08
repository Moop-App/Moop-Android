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
import soup.compose.material.motion.animation.materialSharedAxisZIn
import soup.compose.material.motion.animation.materialSharedAxisZOut
import soup.movie.feature.settings.impl.home.SettingsScreen
import soup.movie.feature.settings.impl.home.SettingsViewModel
import soup.movie.feature.settings.impl.theme.ThemeOptionScreen

private enum class Screen(val route: String) {
    Settings("SettingsScreen"),
    ThemeOption("ThemeEditScreen"),
}

@Composable
fun SettingsNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = Screen.Settings.route,
        enterTransition = { materialSharedAxisZIn(forward = true) },
        exitTransition = { materialSharedAxisZOut(forward = true) },
        popEnterTransition = { materialSharedAxisZIn(forward = false) },
        popExitTransition = { materialSharedAxisZOut(forward = false) },
    ) {
        composable(Screen.Settings.route) {
            val viewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(
                viewModel = viewModel,
                onThemeEditClick = {
                    navController.navigate(Screen.ThemeOption.route)
                },
            )
        }
        composable(Screen.ThemeOption.route) {
            ThemeOptionScreen()
        }
    }
}
