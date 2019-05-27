package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import soup.movie.di.scope.ViewModelKey
import soup.movie.ui.theme.ThemeOptionViewModel

@Module
abstract class ThemeOptionUiModule {

    @Binds
    @IntoMap
    @ViewModelKey(ThemeOptionViewModel::class)
    abstract fun bindThemeOptionViewModel(viewModel: ThemeOptionViewModel): ViewModel
}
