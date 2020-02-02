package soup.movie.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import soup.movie.MovieApplication
import soup.movie.ads.AdsManager
import soup.movie.analytics.EventAnalytics
import soup.movie.device.InAppUpdateManager
import soup.movie.device.ProductAppUpdateManager
import soup.movie.settings.impl.ThemeOptionSetting
import soup.movie.theme.ThemeOptionManager
import soup.movie.theme.ThemeOptionStore
import soup.movie.util.ImageUriProvider
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    fun provideApplication(
        application: MovieApplication
    ): Application = application

    @Provides
    fun provideContext(
        application: MovieApplication
    ): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideImageUriProvider(
        context: Context
    ): ImageUriProvider = ImageUriProvider(context)

    @Singleton
    @Provides
    fun provideEventAnalytics(
        context: Context
    ): EventAnalytics = EventAnalytics(context)

    @Singleton
    @Provides
    fun provideThemeOptionManager(
        themeOptionSetting: ThemeOptionSetting
    ): ThemeOptionManager = ThemeOptionManager(object : ThemeOptionStore {

        override fun save(option: String) {
            themeOptionSetting.set(option)
        }

        override fun restore(): String {
            return themeOptionSetting.get()
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
    ): AdsManager = AdsManager(context)
}
