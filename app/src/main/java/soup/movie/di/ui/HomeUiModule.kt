package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.movie.di.scope.FragmentScope
import soup.movie.di.scope.ViewModelKey
import soup.movie.ui.home.HomeFragment
import soup.movie.ui.home.HomeViewModel
import soup.movie.ui.home.filter.HomeFilterViewModel

@Module
abstract class HomeUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideHomeFragment(): HomeFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeFilterViewModel::class)
    abstract fun bindMovieFilterViewModel(viewModel: HomeFilterViewModel): ViewModel
}
