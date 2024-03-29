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
package soup.movie.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import soup.movie.common.DefaultDispatcher
import soup.movie.common.IoDispatcher
import soup.movie.common.MainDispatcher
import soup.movie.common.MainImmediateDispatcher

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Provides
    @IoDispatcher
    fun providesIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @DefaultDispatcher
    fun providesDefaultDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Provides
    @MainDispatcher
    fun providesMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @Provides
    @MainImmediateDispatcher
    fun providesMainImmediateDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main.immediate
    }
}
