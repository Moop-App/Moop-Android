package soup.movie.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import soup.movie.settings.impl.*
import javax.inject.Singleton

@Module
class SharedPreferencesModule {

    @Singleton
    @Provides
    fun provideLastMainTabSetting(preferences: SharedPreferences):
            LastMainTabSetting =
            LastMainTabSetting(preferences)

    @Singleton
    @Provides
    fun provideAgeFilterSetting(preferences: SharedPreferences):
            AgeFilterSetting =
            AgeFilterSetting(preferences)

    @Singleton
    @Provides
    fun provideTheaterFilterSetting(preferences: SharedPreferences):
            TheaterFilterSetting =
            TheaterFilterSetting(preferences)

    @Singleton
    @Provides
    fun provideTheatersSetting(preferences: SharedPreferences):
            TheatersSetting =
            TheatersSetting(preferences)

    @Singleton
    @Provides
    fun provideUseWebLinkSetting(preferences: SharedPreferences):
            UseWebLinkSetting =
            UseWebLinkSetting(preferences)

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context):
            SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
}
