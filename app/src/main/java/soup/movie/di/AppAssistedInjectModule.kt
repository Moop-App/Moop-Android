package soup.movie.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_AppAssistedInjectModule::class])
abstract class AppAssistedInjectModule
