package soup.movie.di.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.movie.di.scope.FragmentScope
import soup.movie.settings.SettingsFragment
import soup.movie.settings.di.SettingsAssistedInjectModule
import soup.movie.settings.di.SettingsFragmentModule

@Module
interface SettingsUiModule {

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            SettingsFragmentModule::class,
            SettingsAssistedInjectModule::class
        ]
    )
    fun bindSettingsFragment(): SettingsFragment
}
