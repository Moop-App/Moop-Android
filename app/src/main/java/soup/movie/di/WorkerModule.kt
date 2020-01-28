package soup.movie.di

import androidx.work.Configuration
import dagger.Module
import dagger.Provides

@Module(includes = [WorkerBindingModule::class])
class WorkerModule {

    @Provides
    fun provideWorkConfiguration(workerFactory: WorkerFactory): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}
