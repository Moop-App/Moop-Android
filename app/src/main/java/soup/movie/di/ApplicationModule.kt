package soup.movie.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import soup.movie.ads.AdsManager
import soup.movie.ads.AdsManagerImpl
import soup.movie.analytics.EventAnalytics
import soup.movie.analytics.EventAnalyticsImpl
import soup.movie.device.ImageUriProvider
import soup.movie.device.ImageUriProviderImpl
import soup.movie.device.InAppUpdateManager
import soup.movie.device.ProductAppUpdateManager
import soup.movie.settings.AppSettings
import soup.movie.settings.AppSettingsImpl
import soup.movie.theme.ThemeOptionManager
import soup.movie.theme.ThemeOptionStore
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    fun provideContext(
        application: Application
    ): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideImageUriProvider(
        context: Context
    ): ImageUriProvider {
        return ImageUriProviderImpl(context)
    }

    @Singleton
    @Provides
    fun provideEventAnalytics(
        context: Context
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
        context: Context
    ): InAppUpdateManager = ProductAppUpdateManager(context)

    @Singleton
    @Provides
    fun provideAdsManager(
        context: Context
    ): AdsManager {
        return AdsManagerImpl(context)
    }

    @Singleton
    @Provides
    fun provideAppSettings(
        context: Context
    ): AppSettings = AppSettingsImpl(context)
}
