package soup.movie.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import soup.movie.BuildType
import soup.movie.data.MoopRepository
import soup.movie.data.source.local.LocalMoopDataSource
import soup.movie.data.source.local.MoopDao
import soup.movie.data.source.local.MoopDatabase
import soup.movie.data.source.remote.MoopApiService
import soup.movie.data.source.remote.RemoteMoopDataSource
import javax.inject.Singleton

@Module
class MoopRepositoryModule {

    @Singleton
    @Provides
    internal fun provideMoopRepository(localDataSource: LocalMoopDataSource,
                                       remoteDataSource: RemoteMoopDataSource): MoopRepository =
            MoopRepository(localDataSource, remoteDataSource)

    /* Local */

    @Singleton
    @Provides
    internal fun provideLocalDataSource(moopDao: MoopDao): LocalMoopDataSource =
            LocalMoopDataSource(moopDao)

    @Singleton
    @Provides
    internal fun provideMoopDao(moopDatabase: MoopDatabase): MoopDao = moopDatabase.moopDao()

    @Singleton
    @Provides
    internal fun provideDatabase(context: Context): MoopDatabase =
            Room.databaseBuilder(context.applicationContext,
                    MoopDatabase::class.java, "moop.db")
                    .fallbackToDestructiveMigration()
                    .build()

    /* Remote */

    @Singleton
    @Provides
    internal fun provideRemoteDataSource(moopApiService: MoopApiService): RemoteMoopDataSource =
            RemoteMoopDataSource(moopApiService)

    @Singleton
    @Provides
    internal fun provideMoopApiService(rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
                                       okHttpClient: OkHttpClient): MoopApiService =
            Retrofit.Builder()
                    .baseUrl(MoopApiService.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(rxJava2CallAdapterFactory)
                    .client(okHttpClient)
                    .build()
                    .create(MoopApiService::class.java)

    @Singleton
    @Provides
    internal fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory =
            RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    @Singleton
    @Provides
    internal fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient =
            BuildType.addNetworkInterceptor(OkHttpClient.Builder())
                    .addInterceptor(interceptor)
                    .build()

    @Singleton
    @Provides
    internal fun provideOkHttpInterceptor(): Interceptor =
            Interceptor { it.proceed(it.request().newBuilder().build()) }
}
