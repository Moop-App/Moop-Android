package soup.movie.theatermap.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_TheaterMapAssistedInjectModule::class])
abstract class TheaterMapAssistedInjectModule
