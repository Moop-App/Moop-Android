package soup.movie.di

import android.app.Application
import dagger.Module
import dagger.Provides
import soup.movie.data.api.ApiComponent
import soup.movie.data.api.MoopApiService
import soup.movie.data.db.DbComponent
import soup.movie.data.db.LocalMoopDataSource
import soup.movie.data.repository.MoopRepository
import javax.inject.Singleton

@Module
class MoopRepositoryModule {

    @Singleton
    @Provides
    fun provideMoopRepository(
        local: LocalMoopDataSource,
        moopApi: MoopApiService
    ): MoopRepository = MoopRepository(local, moopApi)

    @Singleton
    @Provides
    fun provideLocalDataSource(application: Application): LocalMoopDataSource {
        return DbComponent.factory()
            .create(application)
            .localDataSource()
    }

    @Singleton
    @Provides
    fun provideMoopApi(application: Application): MoopApiService {
        return ApiComponent.factory()
            .create(application)
            .moopApi()
    }
}
