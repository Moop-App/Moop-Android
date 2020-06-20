package soup.movie.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import soup.movie.model.repository.MoopRepository
import javax.inject.Qualifier

@EntryPoint
@InstallIn(ApplicationComponent::class)
interface TheaterMapModuleDependencies {

    fun moopRepository(): MoopRepository
}
