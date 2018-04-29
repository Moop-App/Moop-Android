package soup.movie;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import soup.movie.di.DaggerApplicationComponent;

public class MovieApplication extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        BuildType.init(this);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerApplicationComponent.builder().application(this).build();
    }
}
