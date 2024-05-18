/*
 * Copyright 2022 SOUP
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
package soup.movie.data.network.impl.di

import android.content.Context
import dagger.Binds
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
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import soup.movie.buildconfig.BuildConfig
import soup.movie.data.network.RemoteDataSource
import soup.movie.data.network.impl.MovieApiService
import soup.movie.data.network.impl.OkHttpInterceptors
import soup.movie.data.network.impl.RemoteDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    @Singleton
    fun bindRemoteDataSource(
        remoteDataSourceImpl: RemoteDataSourceImpl,
    ): RemoteDataSource

    companion object {

        private val json = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }

        @Provides
        @Singleton
        internal fun provideMovieApiService(
            okHttpClient: OkHttpClient,
        ): MovieApiService {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
                .client(okHttpClient)
                .build()
                .create(MovieApiService::class.java)
        }

        @Provides
        @Singleton
        internal fun provideOkHttpClient(
            @ApplicationContext context: Context,
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .cache(Cache(context.cacheDir, 1 * 1024 * 1024)) // 1 MB
                .addInterceptor(OkHttpInterceptors.createOkHttpInterceptor())
                .addNetworkInterceptor(OkHttpInterceptors.createOkHttpNetworkInterceptor())
                .build()
        }
    }
}
