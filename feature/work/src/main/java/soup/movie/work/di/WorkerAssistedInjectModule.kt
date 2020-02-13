package soup.movie.work.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_WorkerAssistedInjectModule::class])
abstract class WorkerAssistedInjectModule
