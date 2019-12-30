package soup.movie.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import soup.movie.BuildConfig.API_BASE_URL
import soup.movie.BuildType
import soup.movie.data.MoopRepository
import soup.movie.data.source.local.LocalMoopDataSource
import soup.movie.data.source.local.MoopDao
import soup.movie.data.source.local.MoopDatabase
import soup.movie.data.source.remote.MoopApiService
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
        moopDao: MoopDao
    ): LocalMoopDataSource = LocalMoopDataSource(moopDao)

    @Singleton
    @Provides
    fun provideMoopDao(
        moopDatabase: MoopDatabase
    ): MoopDao = moopDatabase.moopDao()

    @Singleton
    @Provides
    fun provideDatabase(
        context: Context
    ): MoopDatabase = Room
        .databaseBuilder(context.applicationContext, MoopDatabase::class.java, "moop.db")
        .fallbackToDestructiveMigration()
        .build()

    /* Remote */

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        moopApiService: MoopApiService
    ): RemoteMoopDataSource = RemoteMoopDataSource(moopApiService)

    @Singleton
    @Provides
    fun provideMoopApiService(
        okHttpClient: OkHttpClient
    ): MoopApiService = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(MoopApiService::class.java)

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
