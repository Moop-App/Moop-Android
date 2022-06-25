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
package soup.movie.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import soup.movie.startup.WorkManagerInitializer

@Component(dependencies = [InitializerDependencies::class])
interface InitializerComponent {

    fun inject(initializer: WorkManagerInitializer)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(dependencies: InitializerDependencies): Builder
        fun build(): InitializerComponent
    }
}
