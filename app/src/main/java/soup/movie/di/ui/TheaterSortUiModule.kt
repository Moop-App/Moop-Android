package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import soup.movie.di.scope.ViewModelKey
import soup.movie.ui.theater.sort.TheaterSortViewModel

@Module
abstract class TheaterSortUiModule {

    @Binds
    @IntoMap
    @ViewModelKey(TheaterSortViewModel::class)
    abstract fun bindDetailViewModel(viewModel: TheaterSortViewModel): ViewModel
}
