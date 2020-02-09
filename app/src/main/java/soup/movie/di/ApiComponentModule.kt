package soup.movie.di

import android.app.Application
import dagger.Module
import dagger.Provides
import soup.movie.data.api.ApiComponent
import soup.movie.data.api.MoopApiService
import javax.inject.Singleton

@Module
object ApiComponentModule {

    @Singleton
    @Provides
    fun provideMoopApi(
        application: Application
    ): MoopApiService {
        return ApiComponent.factory()
            .create(application)
            .moopApi()
    }
}
