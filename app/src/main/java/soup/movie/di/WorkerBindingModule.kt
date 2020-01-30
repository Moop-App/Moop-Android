package soup.movie.di

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import soup.movie.di.key.WorkerKey
import soup.movie.work.LegacyWorker

@Module
interface WorkerBindingModule {

    @Binds
    @IntoMap
    @WorkerKey(LegacyWorker::class)
    fun bindLegacyWorker(factory: LegacyWorker.Factory): ChildWorkerFactory
}
