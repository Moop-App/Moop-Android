/*
 * Copyright 2024 SOUP
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
package soup.movie.feature.home.impl.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import soup.movie.feature.detail.DetailScreenKey
import soup.movie.feature.home.HomeComposableFactory
import soup.movie.feature.home.HomeScreenKey
import soup.movie.feature.home.impl.HomeComposableFactoryImpl
import soup.movie.feature.home.impl.HomeScreen
import soup.movie.feature.home.impl.favorite.HomeFavoriteScreen
import soup.movie.feature.navigator.EntryProviderInstaller
import soup.movie.feature.navigator.Navigator
import soup.movie.feature.search.SearchScreenKey
import soup.movie.feature.settings.SettingsScreenKey

@Module
@InstallIn(SingletonComponent::class)
interface FeatureHomeModule {

    @Binds
    fun bindsHomeComposableFactory(
        impl: HomeComposableFactoryImpl,
    ): HomeComposableFactory
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object HomeModule {

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<HomeScreenKey.Home>(
            metadata = ListDetailSceneStrategy.listPane("root"),
        ) {
            HomeScreen(
                viewModel = hiltViewModel(),
                onSearchClick = {
                    navigator.navigate(SearchScreenKey.Root)
                },
                onMovieItemClick = {
                    navigator.navigate(DetailScreenKey.Movie(movieId = it.id))
                },
            )
        }
        entry<HomeScreenKey.Favorite>(
            metadata = ListDetailSceneStrategy.listPane("root"),
        ) {
            HomeFavoriteScreen(
                viewModel = hiltViewModel(),
                onSettingsClick = {
                    navigator.navigate(SettingsScreenKey.Settings)
                },
                onItemClick = {
                    navigator.navigate(DetailScreenKey.Movie(movieId = it.id))
                },
            )
        }
    }
}
