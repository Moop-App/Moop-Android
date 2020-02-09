package soup.movie.data.repository.internal

import dagger.Module
import dagger.Provides
import soup.movie.data.api.MoopApiService
import soup.movie.data.db.MoopDatabase
import soup.movie.model.repository.MoopRepository

@Module(includes = [RepositoryModule.Providers::class])
internal class RepositoryModule {

    @Provides
    fun moopRepository(
        moopDb: MoopDatabase,
        moopApi: MoopApiService
    ): MoopRepository {
        return DataMoopRepository(moopDb, moopApi)
    }

    @Module
    internal object Providers
}
