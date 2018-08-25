package soup.movie.di.module;

import dagger.Binds;
import dagger.Module;
import soup.movie.di.scope.ActivityScoped;
import soup.movie.ui.theater.edit.TheaterEditContract;
import soup.movie.ui.theater.edit.TheaterEditPresenter;

@Module
public abstract class TheaterEditModule {

    @ActivityScoped
    @Binds
    abstract TheaterEditContract.Presenter theaterEditPresenter(TheaterEditPresenter presenter);
}
