package soup.movie.core.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import soup.movie.Credentials

@EntryPoint
@InstallIn(SingletonComponent::class)
interface InitializerDependencies {

    fun credentials(): Credentials
}
