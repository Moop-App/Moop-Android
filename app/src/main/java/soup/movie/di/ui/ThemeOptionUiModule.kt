package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.movie.di.key.ViewModelKey
import soup.movie.di.scope.FragmentScope
import soup.movie.ui.theme.ThemeOptionFragment
import soup.movie.ui.theme.ThemeOptionViewModel

@Module
interface ThemeOptionUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    fun bindThemeOptionFragment(): ThemeOptionFragment

    @Binds
    @IntoMap
    @ViewModelKey(ThemeOptionViewModel::class)
    fun bindThemeOptionViewModel(viewModel: ThemeOptionViewModel): ViewModel
}
