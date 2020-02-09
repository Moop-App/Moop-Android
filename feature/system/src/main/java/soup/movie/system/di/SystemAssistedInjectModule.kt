package soup.movie.system.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_SystemAssistedInjectModule::class])
abstract class SystemAssistedInjectModule
