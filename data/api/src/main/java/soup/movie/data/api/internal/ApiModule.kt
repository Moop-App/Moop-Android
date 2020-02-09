package soup.movie.data.api.internal

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import soup.movie.data.api.BuildConfig.API_BASE_URL
import soup.movie.data.api.MoopApiService
import soup.movie.data.api.addNetworkDebuggingInterceptor
import soup.movie.data.api.internal.OkHttpInterceptors.createOkHttpInterceptor
import soup.movie.data.api.internal.OkHttpInterceptors.createOkHttpNetworkInterceptor

@Module(includes = [ApiModule.Providers::class])
internal class ApiModule {

    @Provides
    fun provideMoopApiService(
        okHttpClient: OkHttpClient
    ): MoopApiService {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MoopApiService::class.java)
    }

    @Module
    internal object Providers {

        @Provides
        fun provideOkHttpClient(
            context: Context
        ): OkHttpClient = OkHttpClient.Builder()
            .cache(Cache(context.cacheDir, 1 * 1024 * 1024)) // 1 MB
            .addInterceptor(createOkHttpInterceptor())
            .addNetworkInterceptor(createOkHttpNetworkInterceptor())
            .addNetworkDebuggingInterceptor()
            .build()
    }
}
