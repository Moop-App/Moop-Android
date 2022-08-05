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
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import soup.movie.ads.AdsManager
import soup.movie.ads.AdsManagerImpl
import soup.movie.analytics.EventAnalytics
import soup.movie.analytics.EventAnalyticsImpl
import soup.movie.common.IoDispatcher
import soup.movie.device.ImageUriProvider
import soup.movie.device.ImageUriProviderImpl
import soup.movie.install.InAppUpdateManager
import soup.movie.install.InAppUpdateManagerImpl
import soup.movie.notification.NotificationBuilder
import soup.movie.notification.NotificationBuilderImpl
import soup.movie.settings.AppSettings
import soup.movie.settings.AppSettingsImpl
import soup.movie.theme.ThemeOptionManager
import soup.movie.theme.ThemeOptionStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideImageUriProvider(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): ImageUriProvider {
        return ImageUriProviderImpl(context, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideEventAnalytics(
        @ApplicationContext context: Context
    ): EventAnalytics = EventAnalyticsImpl(context)

    @Singleton
    @Provides
    fun provideThemeOptionManager(
        appSettings: AppSettings
    ): ThemeOptionManager = ThemeOptionManager(object : ThemeOptionStore {

        override fun save(option: String) {
            // TODO: Avoid blocking threads on DataStore
            runBlocking {
                appSettings.setThemeOption(option)
            }
        }

        override fun restore(): String {
            // TODO: Avoid blocking threads on DataStore
            return runBlocking { appSettings.getThemeOption() }
        }
    })

    @Singleton
    @Provides
    fun provideAppUpdateManager(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): InAppUpdateManager {
        return InAppUpdateManagerImpl(context, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideAdsManager(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): AdsManager {
        return AdsManagerImpl(context, ProcessLifecycleOwner.get(), ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideAppSettings(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): AppSettings = AppSettingsImpl(context, ioDispatcher)

    @Singleton
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationBuilder = NotificationBuilderImpl(context)
}
