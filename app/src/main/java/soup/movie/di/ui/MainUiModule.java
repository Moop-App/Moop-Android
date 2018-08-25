package soup.movie.di.ui;

import dagger.Module;
import dagger.Provides;
import soup.movie.di.scope.ActivityScope;
import soup.movie.ui.main.MainContract;
import soup.movie.ui.main.MainPresenter;

@Module
public class MainUiModule {

    @ActivityScope
    @Provides
    MainContract.Presenter presenter() {
        return new MainPresenter();
    }
}
