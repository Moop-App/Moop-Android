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
package soup.movie.feature.detail.impl.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import soup.movie.feature.detail.DetailScreenKey
import soup.movie.feature.detail.impl.DetailPoster
import soup.movie.feature.detail.impl.DetailScreen
import soup.movie.feature.detail.impl.DetailViewModel
import soup.movie.feature.navigator.EntryProviderInstaller
import soup.movie.feature.navigator.Navigator

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureDetailModule {

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<DetailScreenKey.Movie>(
            metadata = ListDetailSceneStrategy.detailPane("root"),
        ) { key ->
            val viewModel = hiltViewModel<DetailViewModel, DetailViewModel.Factory>(
                creationCallback = { factory ->
                    factory.create(key)
                }
            )
            DetailScreen(
                viewModel = viewModel,
                onPosterClick = {
                    navigator.navigate(DetailScreenKey.Poster(posterUrl = it))
                },
            )
        }
        entry<DetailScreenKey.Poster> { key ->
            DetailPoster(
                posterUrl = key.posterUrl,
            )
        }
    }
}
