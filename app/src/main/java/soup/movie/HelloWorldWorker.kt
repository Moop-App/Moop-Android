package soup.movie

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import soup.movie.data.repository.MoopRepository
import soup.movie.di.ChildWorkerFactory
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class HelloWorldWorker(
    appContext: Context,
    params: WorkerParameters,
    private val repository: MoopRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return Result.success()
    }

    class Factory @Inject constructor(
        private val repository: Provider<MoopRepository>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): ListenableWorker {
            return HelloWorldWorker(
                context,
                params,
                repository.get()
            )
        }
    }
}
