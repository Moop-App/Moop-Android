package soup.movie.di.module;

import dagger.Binds;
import dagger.Module;
import soup.movie.di.scope.ActivityScoped;
import soup.movie.ui.detail.DetailContract;
import soup.movie.ui.detail.DetailPresenter;

@Module
public abstract class DetailModule {

    @ActivityScoped
    @Binds
    abstract DetailContract.Presenter detailPresenter(DetailPresenter presenter);
}
