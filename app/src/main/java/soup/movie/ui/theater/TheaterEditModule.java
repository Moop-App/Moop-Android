package soup.movie.ui.theater;

import dagger.Binds;
import dagger.Module;
import soup.movie.di.ActivityScoped;

@Module
public abstract class TheaterEditModule {

    @ActivityScoped
    @Binds
    abstract TheaterEditContract.Presenter theaterEditPresenter(TheaterEditPresenter presenter);
}
