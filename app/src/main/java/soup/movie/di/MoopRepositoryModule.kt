package soup.movie.di

import android.app.Application
import dagger.Module
import dagger.Provides
import soup.movie.data.api.ApiComponent
import soup.movie.data.repository.MoopRepository
import soup.movie.data.source.MoopDataSourceFactory
import javax.inject.Singleton

@Module
class MoopRepositoryModule {

    @Singleton
    @Provides
    fun provideMoopRepository(
        application: Application
    ): MoopRepository = MoopRepository(
        MoopDataSourceFactory.createLocalDataSource(application),
        ApiComponent.factory().create(application).moopApi()
    )
}
