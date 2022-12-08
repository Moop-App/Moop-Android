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
package soup.movie.feature.theme.impl.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import soup.movie.feature.common.settings.AppSettings
import soup.movie.feature.theme.ThemeOptionManager
import soup.movie.feature.theme.impl.ThemeOptionManagerImpl
import soup.movie.feature.theme.impl.ThemeOptionStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FeatureThemeModule {

    @Binds
    @Singleton
    fun provideThemeOptionManager(
        themeOptionManagerImpl: ThemeOptionManagerImpl
    ): ThemeOptionManager

    companion object {

        @Singleton
        @Provides
        fun provideThemeOptionStore(
            appSettings: AppSettings
        ): ThemeOptionStore {
            return object : ThemeOptionStore {

                override fun save(option: String) {
                    // TODO: Avoid blocking threads on DataStore
                    runBlocking { appSettings.setThemeOption(option) }
                }

                override fun restore(): String {
                    // TODO: Avoid blocking threads on DataStore
                    return runBlocking { appSettings.getThemeOption() }
                }
            }
        }
    }
}
