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
interface HomeUiModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [HomeTabUiModule::class])
    fun bindHomeFragment(): HomeFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @FragmentScope
    @ContributesAndroidInjector
    fun bindHomeFilterDialogFragment(): HomeFilterFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeFilterViewModel::class)
    fun bindMovieFilterViewModel(viewModel: HomeFilterViewModel): ViewModel
}

@Module
interface HomeTabUiModule {

    @ChildFragmentScope
    @ContributesAndroidInjector
    fun bindHomeNowFragment(): HomeNowFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeNowViewModel::class)
    fun bindHomeNowViewModel(viewModel: HomeNowViewModel): ViewModel

    @ChildFragmentScope
    @ContributesAndroidInjector
    fun bindHomePlanFragment(): HomePlanFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomePlanViewModel::class)
    fun bindHomePlanViewModel(viewModel: HomePlanViewModel): ViewModel

    @ChildFragmentScope
    @ContributesAndroidInjector
    fun bindHomeFavoriteFragment(): HomeFavoriteFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeFavoriteViewModel::class)
    fun bindHomeFavoriteViewModel(viewModel: HomeFavoriteViewModel): ViewModel
}
