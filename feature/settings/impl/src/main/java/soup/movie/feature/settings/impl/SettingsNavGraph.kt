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
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import soup.movie.feature.settings.impl.home.SettingsScreen
import soup.movie.feature.settings.impl.home.SettingsViewModel
import soup.movie.feature.settings.impl.theme.ThemeOptionScreen
import soup.movie.feature.settings.impl.theme.ThemeOptionViewModel

private sealed interface SettingsScreen : NavKey {

    @Serializable
    data object Home : SettingsScreen

    @Serializable
    data object ThemeOption : SettingsScreen
}

@Composable
fun SettingsNavGraph() {
    // Create navigation state with Settings.Home as the start route
    val navigationState = rememberSettingsNavigationState(
        startRoute = SettingsScreen.Home,
    )

    val navigator = remember { SettingsNavigator(navigationState) }

    // Define entry provider for settings destinations
    val entryProvider = entryProvider<NavKey> {
        entry<SettingsScreen.Home> {
            val viewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(
                viewModel = viewModel,
                onThemeEditClick = {
                    navigator.navigate(SettingsScreen.ThemeOption)
                },
            )
        }
        entry<SettingsScreen.ThemeOption> {
            val viewModel = hiltViewModel<ThemeOptionViewModel>()
            ThemeOptionScreen(viewModel.items)
        }
    }

    // Replace NavHost with NavDisplay
    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() },
    )
}
