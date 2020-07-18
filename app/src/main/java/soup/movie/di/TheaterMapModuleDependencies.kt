package soup.movie.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import soup.movie.model.repository.MoopRepository

@EntryPoint
@InstallIn(SingletonComponent::class)
interface TheaterMapModuleDependencies {

    fun moopRepository(): MoopRepository
}
