package soup.movie.di

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import soup.movie.HelloWorldWorker
import soup.movie.di.key.WorkerKey

@Module
interface WorkerBindingModule {

    @Binds
    @IntoMap
    @WorkerKey(HelloWorldWorker::class)
    fun bindHelloWorldWorker(factory: HelloWorldWorker.Factory): ChildWorkerFactory
}
