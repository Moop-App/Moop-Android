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
import soup.movie.data.source.MoobRepository
import soup.movie.data.source.local.LocalMoobDataSource
import soup.movie.data.source.local.MoobDao
import soup.movie.data.source.local.MoobDatabase
import soup.movie.data.source.remote.MoobApiService
import soup.movie.data.source.remote.RemoteMoobDataSource
import javax.inject.Singleton

@Module
class MoobRepositoryModule {

    @Singleton
    @Provides
    internal fun provideMoobRepository(localDataSource: LocalMoobDataSource,
                                       remoteDataSource: RemoteMoobDataSource): MoobRepository =
            MoobRepository(localDataSource, remoteDataSource)

    /* Local */

    @Singleton
    @Provides
    internal fun provideLocalDataSource(moobDao: MoobDao): LocalMoobDataSource =
            LocalMoobDataSource(moobDao)

    @Singleton
    @Provides
    internal fun provideMoobDao(moobDatabase: MoobDatabase): MoobDao = moobDatabase.moobDao()

    @Singleton
    @Provides
    internal fun provideDatabase(context: Context): MoobDatabase =
            Room.databaseBuilder(context.applicationContext,
                    MoobDatabase::class.java, "moob.db")
                    .build()

    /* Remote */

    @Singleton
    @Provides
    internal fun provideRemoteDataSource(moobApiService: MoobApiService): RemoteMoobDataSource =
            RemoteMoobDataSource(moobApiService)

    @Singleton
    @Provides
    internal fun provideMoobApiService(rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
                                       okHttpClient: OkHttpClient): MoobApiService =
            Retrofit.Builder()
                    .baseUrl(MoobApiService.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(rxJava2CallAdapterFactory)
                    .client(okHttpClient)
                    .build()
                    .create(MoobApiService::class.java)

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
