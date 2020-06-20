package soup.movie.data.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import soup.movie.data.api.MoopApiService
import soup.movie.data.db.MoopDatabase
import soup.movie.data.repository.internal.DataMoopRepository
import soup.movie.model.repository.MoopRepository
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun moopRepository(
        moopDb: MoopDatabase,
        moopApi: MoopApiService
    ): MoopRepository {
        return DataMoopRepository(moopDb, moopApi)
    }
}
