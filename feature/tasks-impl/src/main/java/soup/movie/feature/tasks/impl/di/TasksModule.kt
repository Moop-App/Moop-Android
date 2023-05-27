/*
 * Copyright 2023 SOUP
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
package soup.movie.feature.tasks.impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import soup.movie.feature.tasks.AnnounceOpenDateTasks
import soup.movie.feature.tasks.RecommendMoviesTasks
import soup.movie.feature.tasks.SyncOpenDateTasks
import soup.movie.feature.tasks.impl.AnnounceOpenDateTasksImpl
import soup.movie.feature.tasks.impl.AnnounceOpenDateUseCase
import soup.movie.feature.tasks.impl.AnnounceOpenDateUseCaseImpl
import soup.movie.feature.tasks.impl.RecommendMoviesTasksImpl
import soup.movie.feature.tasks.impl.RecommendMoviesUseCase
import soup.movie.feature.tasks.impl.RecommendMoviesUseCaseImpl
import soup.movie.feature.tasks.impl.SyncOpenDateTasksImpl
import soup.movie.feature.tasks.impl.SyncOpenDateUseCase
import soup.movie.feature.tasks.impl.SyncOpenDateUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface TasksModule {

    @Binds
    fun bindsRecommendMoviesTasks(
        impl: RecommendMoviesTasksImpl
    ): RecommendMoviesTasks

    @Binds
    fun bindsRecommendMoviesUseCase(
        impl: RecommendMoviesUseCaseImpl
    ): RecommendMoviesUseCase

    @Binds
    fun bindsAnnounceOpenDateTasks(
        impl: AnnounceOpenDateTasksImpl
    ): AnnounceOpenDateTasks

    @Binds
    fun bindsAnnounceOpenDateUseCase(
        impl: AnnounceOpenDateUseCaseImpl
    ): AnnounceOpenDateUseCase

    @Binds
    fun bindsSyncOpenDateTasks(
        impl: SyncOpenDateTasksImpl
    ): SyncOpenDateTasks

    @Binds
    fun bindsSyncOpenDateUseCase(
        impl: SyncOpenDateUseCaseImpl
    ): SyncOpenDateUseCase
}
