package soup.movie.di

import android.content.Context
import dagger.Module
import dagger.Provides
import soup.movie.data.api.MoopApiService
import soup.movie.data.db.MoopDatabase
import soup.movie.data.repository.RepositoryComponent
import soup.movie.model.repository.MoopRepository
import javax.inject.Singleton

@Module
class RepositoryComponentModule {

    @Singleton
    @Provides
    fun provideMoopRepository(
        repositoryComponent: RepositoryComponent
    ): MoopRepository {
        return repositoryComponent.moopRepository()
    }

    @Singleton
    @Provides
    fun provideRepositoryComponent(
        context: Context,
        moopApi: MoopApiService,
        moopDb: MoopDatabase
    ): RepositoryComponent {
        return RepositoryComponent.factory().create(
            context = context,
            moopApi = moopApi,
            moopDb = moopDb
        )
    }
}
