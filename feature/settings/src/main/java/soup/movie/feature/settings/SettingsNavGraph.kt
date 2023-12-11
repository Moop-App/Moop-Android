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
package soup.movie.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import soup.compose.material.motion.animation.materialSharedAxisZIn
import soup.compose.material.motion.animation.materialSharedAxisZOut
import soup.movie.core.designsystem.showToast
import soup.movie.core.external.Moop
import soup.movie.feature.theater.edit.TheaterEditScreen
import soup.movie.feature.theater.edit.TheaterEditViewModel
import soup.movie.feature.theater.sort.TheaterSortScreen
import soup.movie.feature.theater.sort.TheaterSortViewModel
import soup.movie.feature.theme.ThemeOptionScreen
import soup.movie.feature.theme.ThemeOptionViewModel
import soup.movie.resources.R

private enum class Screen(val route: String) {
    Settings("SettingsScreen"),
    ThemeOption("ThemeEditScreen"),
    TheaterSort("TheaterSortScreen"),
    TheaterEdit("TheaterEditScreen"),
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
            val context = LocalContext.current
            val viewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(
                viewModel = viewModel,
                onThemeEditClick = {
                    navController.navigate(Screen.ThemeOption.route)
                },
                onTheaterEditClick = {
                    navController.navigate(Screen.TheaterSort.route)
                },
                onVersionClick = {
                    if (it.isLatest) {
                        context.showToast(R.string.settings_version_toast)
                    } else {
                        Moop.executePlayStore(context)
                    }
                },
                onMarketIconClick = {
                    Moop.executePlayStore(context)
                },
            )
        }
        composable(Screen.ThemeOption.route) {
            val viewModel = hiltViewModel<ThemeOptionViewModel>()
            ThemeOptionScreen(viewModel = viewModel)
        }
        composable(Screen.TheaterSort.route) {
            val viewModel = hiltViewModel<TheaterSortViewModel>()
            TheaterSortScreen(
                viewModel = viewModel,
                upPress = {
                    navController.navigateUp()
                },
                onAddItemClick = {
                    navController.navigate(Screen.TheaterEdit.route)
                },
            )
        }
        composable(Screen.TheaterEdit.route) {
            val viewModel = hiltViewModel<TheaterEditViewModel>()
            TheaterEditScreen(
                viewModel = viewModel,
                upPress = { navController.navigateUp() },
            )
        }
    }
}
