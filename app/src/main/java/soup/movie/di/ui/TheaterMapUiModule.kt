package soup.movie.di.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import soup.movie.di.scope.FragmentScope
import soup.movie.ui.map.TheaterMapFragment
import soup.movie.ui.map.di.TheaterMapAssistedInjectModule
import soup.movie.ui.map.di.TheaterMapFragmentModule

@Module
interface TheaterMapUiModule {

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            TheaterMapFragmentModule::class,
            TheaterMapAssistedInjectModule::class
        ]
    )
    fun bindTheaterMapFragment(): TheaterMapFragment
}
