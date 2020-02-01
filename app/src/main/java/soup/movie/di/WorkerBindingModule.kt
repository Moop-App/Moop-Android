package soup.movie.di

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import soup.movie.di.key.WorkerKey
import soup.movie.work.LegacyWorker
import soup.movie.work.OpenDateAlarmWorker

@Module
interface WorkerBindingModule {

    @Binds
    @IntoMap
    @WorkerKey(LegacyWorker::class)
    fun bindLegacyWorker(factory: LegacyWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(OpenDateAlarmWorker::class)
    fun bindOpenDateAlarmWorker(factory: OpenDateAlarmWorker.Factory): ChildWorkerFactory
}
