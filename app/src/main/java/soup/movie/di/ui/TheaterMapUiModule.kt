package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.movie.di.scope.FragmentScope
import soup.movie.di.scope.ViewModelKey
import soup.movie.ui.map.TheaterMapFragment
import soup.movie.ui.map.TheaterMapViewModel

@Module
abstract class TheaterMapUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindTheaterMapFragment(): TheaterMapFragment

    @Binds
    @IntoMap
    @ViewModelKey(TheaterMapViewModel::class)
    abstract fun bindTheaterMapViewModel(viewModel: TheaterMapViewModel): ViewModel
}
