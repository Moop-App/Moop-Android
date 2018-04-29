package soup.movie.ui.main.settings;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import soup.movie.di.FragmentScoped;

@Module
public abstract class SettingsModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SettingsFragment settingsFragment();

    @FragmentScoped
    @Binds
    abstract SettingsContract.Presenter settingsPresenter(SettingsPresenter presenter);
}
