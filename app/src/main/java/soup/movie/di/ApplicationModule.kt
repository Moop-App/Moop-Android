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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import soup.movie.ads.AdsConfigImpl
import soup.movie.analytics.EventAnalyticsImpl
import soup.movie.common.IoDispatcher
import soup.movie.core.ads.AdsConfig
import soup.movie.core.analytics.EventAnalytics
import soup.movie.core.imageloading.ImageUriProvider
import soup.movie.feature.common.install.InAppUpdateManager
import soup.movie.feature.common.install.InAppUpdateManagerImpl
import soup.movie.feature.work.NotificationBuilder
import soup.movie.imageloading.ImageUriProviderImpl
import soup.movie.notification.NotificationBuilderImpl
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
    fun provideAppUpdateManager(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): InAppUpdateManager {
        return InAppUpdateManagerImpl(context, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideAdsConfig(
        @ApplicationContext context: Context,
    ): AdsConfig {
        return AdsConfigImpl(context)
    }

    @Singleton
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationBuilder = NotificationBuilderImpl(context)
}
