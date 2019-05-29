package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.movie.di.scope.FragmentScope
import soup.movie.di.scope.ViewModelKey
import soup.movie.ui.main.home.HomeFragment
import soup.movie.ui.main.home.HomeViewModel
import soup.movie.ui.main.home.filter.HomeFilterFragment
import soup.movie.ui.main.home.filter.HomeFilterViewModel

@Module
abstract class HomeUiModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideHomeFragment(): HomeFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideMovieFilterFragment(): HomeFilterFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeFilterViewModel::class)
    abstract fun bindMovieFilterViewModel(viewModel: HomeFilterViewModel): ViewModel
}