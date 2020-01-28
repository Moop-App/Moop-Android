package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.movie.di.key.ViewModelKey
import soup.movie.di.scope.FragmentScope
import soup.movie.ui.settings.SettingsFragment
import soup.movie.ui.settings.SettingsViewModel

@Module
interface SettingsUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    fun bindSettingsFragment(): SettingsFragment

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}
