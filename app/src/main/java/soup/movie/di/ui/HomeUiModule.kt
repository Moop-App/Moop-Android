package soup.movie.di.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.movie.di.scope.FragmentScope
import soup.movie.home.HomeFragment
import soup.movie.home.di.HomeAssistedInjectModule
import soup.movie.home.di.HomeFilterFragmentModule
import soup.movie.home.di.HomeFragmentModule
import soup.movie.home.filter.HomeFilterFragment

@Module
interface HomeUiModule {

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            HomeFragmentModule::class,
            HomeAssistedInjectModule::class
        ]
    )
    fun bindHomeFragment(): HomeFragment

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            HomeFilterFragmentModule::class,
            HomeAssistedInjectModule::class
        ]
    )
    fun bindHomeFilterDialogFragment(): HomeFilterFragment
}
