/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.data.api

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
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
            .addConverterFactory(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                }.asConverterFactory(MediaType.get("application/json"))
            )
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
