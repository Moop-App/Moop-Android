package soup.movie.ui.main;

import dagger.Binds;
import dagger.Module;
import soup.movie.di.ActivityScoped;

@Module
public abstract class MainModule {

    @ActivityScoped
    @Binds
    abstract MainContract.Presenter mainPresenter(MainPresenter presenter);
}
