package soup.movie.theatermap.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import soup.movie.di.TheaterMapModuleDependencies
import soup.movie.theatermap.TheaterMapFragment

@Component(dependencies = [TheaterMapModuleDependencies::class])
interface TheaterMapComponent {

    fun inject(fragment: TheaterMapFragment)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(theaterMapModuleDependencies: TheaterMapModuleDependencies): Builder
        fun build(): TheaterMapComponent
    }
}
