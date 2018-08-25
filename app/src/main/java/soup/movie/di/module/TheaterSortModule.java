package soup.movie.di.module;

import dagger.Binds;
import dagger.Module;
import soup.movie.di.scope.ActivityScoped;
import soup.movie.ui.theater.sort.TheaterSortContract;
import soup.movie.ui.theater.sort.TheaterSortPresenter;

@Module
public abstract class TheaterSortModule {

    @ActivityScoped
    @Binds
    abstract TheaterSortContract.Presenter theaterSortPresenter(TheaterSortPresenter presenter);
}
