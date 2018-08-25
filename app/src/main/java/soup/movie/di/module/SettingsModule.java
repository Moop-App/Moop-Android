package soup.movie.di.module;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import soup.movie.di.scope.FragmentScoped;
import soup.movie.ui.main.settings.SettingsContract;
import soup.movie.ui.main.settings.SettingsFragment;
import soup.movie.ui.main.settings.SettingsPresenter;

@Module
public abstract class SettingsModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SettingsFragment settingsFragment();

    @FragmentScoped
    @Binds
    abstract SettingsContract.Presenter settingsPresenter(SettingsPresenter presenter);
}
