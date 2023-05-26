package soup.movie.feature.tasks.impl

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.EntryPointAccessors
import soup.movie.feature.tasks.impl.di.DaggerInitializerComponent
import soup.movie.feature.tasks.impl.di.InitializerDependencies
import javax.inject.Inject

class WorkManagerInitializer : Initializer<WorkManager> {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun create(context: Context): WorkManager {
        DaggerInitializerComponent.builder()
            .context(context)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    InitializerDependencies::class.java
                )
            )
            .build()
            .inject(this)

        WorkManager.initialize(
            context,
            Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()
        )
        return WorkManager.getInstance(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
