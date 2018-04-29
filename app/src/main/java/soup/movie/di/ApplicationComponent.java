package soup.movie.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import soup.movie.MovieApplication;
import soup.movie.data.MovieRepository;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ApplicationModule.class,
        ActivityBindingModule.class,
        MovieRepositoryModule.class,
        SharedPreferencesModule.class
})
public interface ApplicationComponent extends AndroidInjector<MovieApplication> {

    MovieRepository getMovieRepository();

    @Component.Builder
    interface Builder {

        @BindsInstance
        ApplicationComponent.Builder application(Application application);

        ApplicationComponent build();
    }
}
