package soup.movie.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import soup.movie.ui.detail.DetailActivity;
import soup.movie.ui.detail.DetailModule;
import soup.movie.ui.main.MainActivity;
import soup.movie.ui.main.MainModule;
import soup.movie.ui.main.now.NowModule;
import soup.movie.ui.main.plan.PlanModule;
import soup.movie.ui.main.settings.SettingsModule;
import soup.movie.ui.theater.edit.TheaterEditActivity;
import soup.movie.ui.theater.edit.TheaterEditModule;
import soup.movie.ui.theater.sort.TheaterSortActivity;
import soup.movie.ui.theater.sort.TheaterSortModule;

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
