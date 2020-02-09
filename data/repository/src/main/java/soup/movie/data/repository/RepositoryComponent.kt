package soup.movie.data.repository

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import soup.movie.data.api.MoopApiService
import soup.movie.data.db.MoopDatabase
import soup.movie.data.repository.internal.RepositoryModule
import soup.movie.model.repository.MoopRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class])
interface RepositoryComponent {

    fun moopRepository(): MoopRepository

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance moopApi: MoopApiService,
            @BindsInstance moopDb: MoopDatabase
        ): RepositoryComponent
    }

    companion object {
        fun factory(): Factory = DaggerRepositoryComponent.factory()
    }
}
