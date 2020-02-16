package soup.movie.theatermap.di

import dagger.Component
import soup.movie.di.ApplicationComponent
import soup.movie.di.scope.FragmentScope
import soup.movie.theatermap.TheaterMapFragment

@FragmentScope
@Component(
    modules = [
        TheaterMapModule::class,
        TheaterMapAssistedInjectModule::class
    ],
    dependencies = [ApplicationComponent::class]
)
interface TheaterMapComponent {

    fun inject(fragment: TheaterMapFragment)

    @Component.Factory
    interface Factory {
        fun create(
            applicationComponent: ApplicationComponent,
            theaterMapModule: TheaterMapModule
        ): TheaterMapComponent
    }
}
