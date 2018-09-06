package soup.movie.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import soup.movie.settings.impl.LastMainTabSetting
import soup.movie.settings.impl.TheaterSetting
import soup.movie.settings.impl.UsePaletteThemeSetting
import javax.inject.Singleton

@Module
class SharedPreferencesModule {

    @Singleton
    @Provides
    fun provideLastMainTabSetting(preferences: SharedPreferences): LastMainTabSetting =
            LastMainTabSetting(preferences)

    @Singleton
    @Provides
    fun provideTheaterSetting(preferences: SharedPreferences): TheaterSetting =
            TheaterSetting(preferences)

    @Singleton
    @Provides
    fun provideUsePaletteThemeSetting(preferences: SharedPreferences): UsePaletteThemeSetting =
            UsePaletteThemeSetting(preferences)

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
}
