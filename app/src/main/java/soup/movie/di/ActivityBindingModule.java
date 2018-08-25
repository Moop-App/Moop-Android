package soup.movie.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import soup.movie.di.scope.ActivityScope;
import soup.movie.di.ui.DetailUiModule;
import soup.movie.di.ui.MainTabUiModule;
import soup.movie.di.ui.MainUiModule;
import soup.movie.di.ui.TheaterEditUiModule;
import soup.movie.di.ui.TheaterSortUiModule;
import soup.movie.ui.detail.DetailActivity;
import soup.movie.ui.main.MainActivity;
import soup.movie.ui.theater.edit.TheaterEditActivity;
import soup.movie.ui.theater.sort.TheaterSortActivity;

@Module
public abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = {
            MainUiModule.class,
            MainTabUiModule.class
    })
    abstract MainActivity mainActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = DetailUiModule.class)
    abstract DetailActivity detailActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = TheaterSortUiModule.class)
    abstract TheaterSortActivity theaterSortActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = TheaterEditUiModule.class)
    abstract TheaterEditActivity theaterEditActivity();
}
