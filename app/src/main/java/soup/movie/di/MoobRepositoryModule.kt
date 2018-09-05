package soup.movie.di

import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import soup.movie.BuildType
import soup.movie.data.IMoobDataSource
import soup.movie.data.MoobDataSource
import soup.movie.data.MoobRepository
import soup.movie.data.service.MoobApiService
import javax.inject.Singleton

@Module
class MoobRepositoryModule {

    @Singleton
    @Provides
    internal fun provideMoobRepository(remoteDataSource: IMoobDataSource): MoobRepository =
            MoobRepository(remoteDataSource)

    @Singleton
    @Provides
    internal fun provideMoobDataSource(moobApiService: MoobApiService): IMoobDataSource =
            MoobDataSource(moobApiService)

    @Singleton
    @Provides
    internal fun provideMoobApiService(gsonConverterFactory: GsonConverterFactory,
                                       rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
                                       okHttpClient: OkHttpClient): MoobApiService =
            Retrofit.Builder()
                    .baseUrl(MoobApiService.API_BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJava2CallAdapterFactory)
                    .client(okHttpClient)
                    .build()
                    .create(MoobApiService::class.java)

    @Singleton
    @Provides
    internal fun provideGsonConverterFactory(): GsonConverterFactory =
            GsonConverterFactory.create()

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
