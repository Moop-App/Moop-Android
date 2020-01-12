package soup.movie.di

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import soup.movie.BuildConfig.API_BASE_URL
import soup.movie.BuildType
import soup.movie.data.MoopRepository
import soup.movie.data.source.MoopDataSourceFactory
import soup.movie.data.source.local.LocalMoopDataSource
import soup.movie.data.source.remote.RemoteMoopDataSource
import soup.movie.data.util.OkHttpInterceptors.provideOkHttpInterceptor
import soup.movie.data.util.OkHttpInterceptors.provideOkHttpNetworkInterceptor
import javax.inject.Singleton

@Module
class MoopRepositoryModule {

    @Singleton
    @Provides
    fun provideMoopRepository(
        localDataSource: LocalMoopDataSource,
        remoteDataSource: RemoteMoopDataSource
    ): MoopRepository = MoopRepository(localDataSource, remoteDataSource)

    /* Local */

    @Singleton
    @Provides
    fun provideLocalDataSource(
        context: Context
    ): LocalMoopDataSource = MoopDataSourceFactory.createLocalDataSource(context)

    /* Remote */

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        okHttpClient: OkHttpClient
    ): RemoteMoopDataSource = RemoteMoopDataSource(
        MoopDataSourceFactory.createMoopApiService(API_BASE_URL, okHttpClient)
    )

    @Singleton
    @Provides
    fun provideOkHttpClient(
        context: Context
    ): OkHttpClient = BuildType
        .addNetworkInterceptor(OkHttpClient.Builder())
        .cache(Cache(context.cacheDir, 1 * 1024 * 1024)) // 1 MB
        .addInterceptor(provideOkHttpInterceptor())
        .addNetworkInterceptor(provideOkHttpNetworkInterceptor())
        .build()
}
