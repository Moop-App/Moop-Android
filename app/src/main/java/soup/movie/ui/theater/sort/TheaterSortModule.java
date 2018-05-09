package soup.movie.ui.theater.sort;

import dagger.Binds;
import dagger.Module;
import soup.movie.di.ActivityScoped;

@Module
public abstract class TheaterSortModule {

    @ActivityScoped
    @Binds
    abstract TheaterSortContract.Presenter theaterSortPresenter(TheaterSortPresenter presenter);
}
