package soup.movie;

import android.app.Application;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import soup.movie.di.DaggerApplicationComponent;
import soup.movie.util.TheaterUtil;

public class MovieApplication extends DaggerApplication {

    private static Application sInstance;

    public MovieApplication() {
        super();
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BuildType.init(this);
        TheaterUtil.loadAsync(null); // pre-load
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerApplicationComponent.builder().application(this).build();
    }

    public static Application getInstance() {
        return sInstance;
    }
}
