package soup.movie.di.module;

import dagger.Binds;
import dagger.Module;
import soup.movie.di.scope.ActivityScoped;
import soup.movie.ui.main.MainContract;
import soup.movie.ui.main.MainPresenter;

@Module
public abstract class MainModule {

    @ActivityScoped
    @Binds
    abstract MainContract.Presenter mainPresenter(MainPresenter presenter);
}
