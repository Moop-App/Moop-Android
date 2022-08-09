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
package soup.movie.data.repository.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import soup.movie.common.IoDispatcher
import soup.movie.data.database.LocalDataSource
import soup.movie.data.network.RemoteDataSource
import soup.movie.data.repository.MovieRepository
import soup.movie.data.repository.TheaterRepository
import soup.movie.data.repository.internal.MovieRepositoryImpl
import soup.movie.data.repository.internal.TheaterRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        local: LocalDataSource,
        remote: RemoteDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): MovieRepository {
        return MovieRepositoryImpl(local, remote, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideTheaterRepository(
        local: LocalDataSource,
        remote: RemoteDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): TheaterRepository {
        return TheaterRepositoryImpl(local, remote, ioDispatcher)
    }
}
