package soup.movie.theater.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_TheaterAssistedInjectModule::class])
abstract class TheaterAssistedInjectModule
