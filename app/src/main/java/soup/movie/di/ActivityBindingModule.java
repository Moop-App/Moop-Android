package soup.movie.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import soup.movie.di.module.DetailModule;
import soup.movie.di.module.MainModule;
import soup.movie.di.module.NowModule;
import soup.movie.di.module.PlanModule;
import soup.movie.di.module.SettingsModule;
import soup.movie.di.module.TheaterEditModule;
import soup.movie.di.module.TheaterSortModule;
import soup.movie.di.scope.ActivityScoped;
import soup.movie.ui.detail.DetailActivity;
import soup.movie.ui.main.MainActivity;
import soup.movie.ui.theater.edit.TheaterEditActivity;
import soup.movie.ui.theater.sort.TheaterSortActivity;

@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = {
            MainModule.class,
            NowModule.class,
            PlanModule.class,
            SettingsModule.class
    })
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = DetailModule.class)
    abstract DetailActivity detailActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = TheaterSortModule.class)
    abstract TheaterSortActivity theaterSortActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = TheaterEditModule.class)
    abstract TheaterEditActivity theaterEditActivity();
}
