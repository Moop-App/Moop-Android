package soup.movie.di.ui;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import soup.movie.data.MoobRepository;
import soup.movie.di.scope.FragmentScope;
import soup.movie.settings.TheaterSetting;
import soup.movie.ui.main.now.NowContract;
import soup.movie.ui.main.now.NowFragment;
import soup.movie.ui.main.now.NowPresenter;
import soup.movie.ui.main.plan.PlanContract;
import soup.movie.ui.main.plan.PlanFragment;
import soup.movie.ui.main.plan.PlanPresenter;
import soup.movie.ui.main.settings.SettingsContract;
import soup.movie.ui.main.settings.SettingsFragment;
import soup.movie.ui.main.settings.SettingsPresenter;

@Module
public abstract class MainTabUiModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = NowModule.class)
    abstract NowFragment provideNowFragment();

    @Module
    public static class NowModule {

        @FragmentScope
        @Provides
        NowContract.Presenter presenter(MoobRepository moobRepository) {
            return new NowPresenter(moobRepository);
        }
    }

    @FragmentScope
    @ContributesAndroidInjector(modules = PlanModule.class)
    abstract PlanFragment providePlanFragment();

    @Module
    public static class PlanModule {

        @FragmentScope
        @Provides
        PlanContract.Presenter presenter(MoobRepository moobRepository) {
            return new PlanPresenter(moobRepository);
        }
    }

    @FragmentScope
    @ContributesAndroidInjector(modules = SettingsModule.class)
    abstract SettingsFragment provideSettingsFragment();

    @Module
    public static class SettingsModule {

        @FragmentScope
        @Provides
        SettingsContract.Presenter presenter(TheaterSetting theaterSetting) {
            return new SettingsPresenter(theaterSetting);
        }
    }
}
