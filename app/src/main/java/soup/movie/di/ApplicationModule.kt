package soup.movie.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import soup.movie.ads.AdsManager
import soup.movie.ads.AdsManagerImpl
import soup.movie.analytics.EventAnalytics
import soup.movie.analytics.EventAnalyticsImpl
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
@InstallIn(ApplicationComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideImageUriProvider(
        @ApplicationContext context: Context
    ): ImageUriProvider {
        return ImageUriProviderImpl(context)
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
            appSettings.themeOption = option
        }

        override fun restore(): String {
            return appSettings.themeOption
        }
    })

    @Singleton
    @Provides
    fun provideAppUpdateManager(
        @ApplicationContext context: Context
    ): InAppUpdateManager = InAppUpdateManagerImpl(context)

    @Singleton
    @Provides
    fun provideAdsManager(
        @ApplicationContext context: Context
    ): AdsManager {
        return AdsManagerImpl(context)
    }

    @Singleton
    @Provides
    fun provideAppSettings(
        @ApplicationContext context: Context
    ): AppSettings = AppSettingsImpl(context)

    @Singleton
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationBuilder = NotificationBuilderImpl(context)
}
