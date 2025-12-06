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
package soup.movie.feature.settings.impl.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import soup.movie.feature.navigator.EntryProviderInstaller
import soup.movie.feature.navigator.Navigator
import soup.movie.feature.settings.SettingsScreenKey
import soup.movie.feature.settings.impl.home.SettingsScreen
import soup.movie.feature.settings.impl.home.SettingsViewModel
import soup.movie.feature.settings.impl.theme.ThemeOptionScreen
import soup.movie.feature.settings.impl.theme.ThemeOptionViewModel

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureSettingsModule {
    private const val SCENE_KEY = "settings"

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<SettingsScreenKey.Root>(
            metadata = ListDetailSceneStrategy.listPane(SCENE_KEY),
        ) {
            val viewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(
                viewModel = viewModel,
                onThemeEditClick = {
                    navigator.navigate(SettingsScreenKey.ThemeOption)
                },
            )
        }
        entry<SettingsScreenKey.ThemeOption>(
            metadata = ListDetailSceneStrategy.detailPane(SCENE_KEY),
        ) {
            val viewModel = hiltViewModel<ThemeOptionViewModel>()
            ThemeOptionScreen(viewModel.items)
        }
    }
}
