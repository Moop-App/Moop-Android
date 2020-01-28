package soup.movie.di.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import soup.movie.di.key.ViewModelKey
import soup.movie.di.scope.ChildFragmentScope
import soup.movie.di.scope.FragmentScope
import soup.movie.ui.home.HomeFragment
import soup.movie.ui.home.HomeViewModel
import soup.movie.ui.home.favorite.HomeFavoriteFragment
import soup.movie.ui.home.favorite.HomeFavoriteViewModel
import soup.movie.ui.home.filter.HomeFilterFragment
import soup.movie.ui.home.filter.HomeFilterViewModel
import soup.movie.ui.home.now.HomeNowFragment
import soup.movie.ui.home.now.HomeNowViewModel
import soup.movie.ui.home.plan.HomePlanFragment
import soup.movie.ui.home.plan.HomePlanViewModel

@Module
abstract class HomeUiModule {

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            HomeTabUiModule::class
        ]
    )
    abstract fun bindHomeFragment(): HomeFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindHomeFilterDialogFragment(): HomeFilterFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeFilterViewModel::class)
    abstract fun bindMovieFilterViewModel(viewModel: HomeFilterViewModel): ViewModel
}

@Module
abstract class HomeTabUiModule {

    @ChildFragmentScope
    @ContributesAndroidInjector
    abstract fun bindHomeNowFragment(): HomeNowFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeNowViewModel::class)
    abstract fun bindHomeNowViewModel(viewModel: HomeNowViewModel): ViewModel

    @ChildFragmentScope
    @ContributesAndroidInjector
    abstract fun bindHomePlanFragment(): HomePlanFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomePlanViewModel::class)
    abstract fun bindHomePlanViewModel(viewModel: HomePlanViewModel): ViewModel

    @ChildFragmentScope
    @ContributesAndroidInjector
    abstract fun bindHomeFavoriteFragment(): HomeFavoriteFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeFavoriteViewModel::class)
    abstract fun bindHomeFavoriteViewModel(viewModel: HomeFavoriteViewModel): ViewModel
}
