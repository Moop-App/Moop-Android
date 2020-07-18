package soup.movie.data.api

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import soup.movie.data.api.BuildConfig.API_BASE_URL
import soup.movie.data.api.internal.OkHttpInterceptors.createOkHttpInterceptor
import soup.movie.data.api.internal.OkHttpInterceptors.createOkHttpNetworkInterceptor
import javax.inject.Singleton

@Module(includes = [ApiModule.Providers::class])
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideMoopApiService(
        okHttpClient: OkHttpClient
    ): MoopApiService {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MoopApiService::class.java)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    internal object Providers {

        @Provides
        @Singleton
        fun provideOkHttpClient(
            @ApplicationContext context: Context
        ): OkHttpClient = OkHttpClient.Builder()
            .cache(Cache(context.cacheDir, 1 * 1024 * 1024)) // 1 MB
            .addInterceptor(createOkHttpInterceptor())
            .addNetworkInterceptor(createOkHttpNetworkInterceptor())
            .build()
    }
}
