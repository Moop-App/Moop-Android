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
package soup.movie.testing.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher
import soup.movie.common.DefaultDispatcher
import soup.movie.common.IoDispatcher
import soup.movie.common.MainDispatcher
import soup.movie.common.MainImmediateDispatcher
import soup.movie.common.di.DispatchersModule

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatchersModule::class],
)
object TestDispatchersModule {

    @Provides
    @IoDispatcher
    fun providesIoDispatcher(testDispatcher: TestDispatcher): CoroutineDispatcher {
        return testDispatcher
    }

    @Provides
    @DefaultDispatcher
    fun providesDefaultDispatcher(testDispatcher: TestDispatcher): CoroutineDispatcher {
        return testDispatcher
    }

    @Provides
    @MainDispatcher
    fun providesMainDispatcher(testDispatcher: TestDispatcher): CoroutineDispatcher {
        return testDispatcher
    }

    @Provides
    @MainImmediateDispatcher
    fun providesMainImmediateDispatcher(testDispatcher: TestDispatcher): CoroutineDispatcher {
        return testDispatcher
    }
}
