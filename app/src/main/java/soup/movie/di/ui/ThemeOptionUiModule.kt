package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.movie.di.scope.FragmentScope
import soup.movie.di.scope.ViewModelKey
import soup.movie.ui.theme.ThemeOptionFragment
import soup.movie.ui.theme.ThemeOptionViewModel

@Module
abstract class ThemeOptionUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideThemeOptionFragment(): ThemeOptionFragment

    @Binds
    @IntoMap
    @ViewModelKey(ThemeOptionViewModel::class)
    abstract fun bindThemeOptionViewModel(viewModel: ThemeOptionViewModel): ViewModel
}
