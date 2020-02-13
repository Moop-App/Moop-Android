package soup.movie.work.di

import androidx.work.Configuration
import dagger.Module
import dagger.Provides

@Module(includes = [WorkerBindingModule::class, WorkerAssistedInjectModule::class])
class WorkerModule {

    @Provides
    fun provideWorkConfiguration(workerFactory: WorkerFactory): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}
