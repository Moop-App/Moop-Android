package soup.movie.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import soup.movie.settings.TheaterSetting;

@Module
public class SharedPreferencesModule {

    @Singleton
    @Provides
    TheaterSetting provideTheaterSetting(SharedPreferences preferences) {
        return new TheaterSetting(preferences);
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
