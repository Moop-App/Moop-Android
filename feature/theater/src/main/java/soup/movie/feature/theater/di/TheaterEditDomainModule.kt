/*
 * Copyright 2021 SOUP
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
package soup.movie.feature.theater.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import soup.movie.data.repository.TheaterRepository
import soup.movie.data.settings.AppSettings
import soup.movie.feature.theater.edit.TheaterEditManager

@Module
@InstallIn(ViewModelComponent::class)
class TheaterEditDomainModule {

    @Provides
    @ViewModelScoped
    fun provideTheaterEditManager(
        repository: TheaterRepository,
        appSettings: AppSettings,
    ): TheaterEditManager {
        return TheaterEditManager(
            repository,
            appSettings,
        )
    }
}
