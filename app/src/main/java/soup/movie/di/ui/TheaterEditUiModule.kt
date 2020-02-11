package soup.movie.di.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.movie.di.scope.FragmentScope
import soup.movie.theater.di.TheaterAssistedInjectModule
import soup.movie.theater.di.TheaterEditDomainModule
import soup.movie.theater.di.TheaterEditFragmentModule
import soup.movie.theater.di.TheaterSortFragmentModule
import soup.movie.theater.edit.TheaterEditFragment
import soup.movie.theater.sort.TheaterSortFragment

@Module
interface TheaterEditUiModule {

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            TheaterSortFragmentModule::class,
            TheaterAssistedInjectModule::class
        ]
    )
    fun bindTheaterSortFragment(): TheaterSortFragment

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            TheaterEditFragmentModule::class,
            TheaterEditDomainModule::class,
            TheaterAssistedInjectModule::class
        ]
    )
    fun bindTheaterEditFragment(): TheaterEditFragment
}
