package soup.movie.ui.main.now;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import soup.movie.di.FragmentScoped;

@Module
public abstract class NowModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract NowFragment nowFragment();

    @FragmentScoped
    @Binds
    abstract NowContract.Presenter nowPresenter(NowPresenter presenter);
}
