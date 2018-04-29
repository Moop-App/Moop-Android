package soup.movie.ui.detail;

import dagger.Binds;
import dagger.Module;
import soup.movie.di.ActivityScoped;

@Module
public abstract class DetailModule {

    @ActivityScoped
    @Binds
    abstract DetailContract.Presenter detailPresenter(DetailPresenter presenter);
}
