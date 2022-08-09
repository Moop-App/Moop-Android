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
package soup.movie.data.network.impl

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import soup.movie.data.network.RemoteDataSource

object RemoteDataSourceFactory {

    fun create(context: Context): RemoteDataSource {
        return RemoteDataSourceImpl(
            Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                    }.asConverterFactory(MediaType.get("application/json"))
                )
                .client(createOkHttpClient(context.applicationContext))
                .build()
                .create(MovieApiService::class.java)
        )
    }

    private fun createOkHttpClient(
        context: Context
    ): OkHttpClient = OkHttpClient.Builder()
        .cache(Cache(context.cacheDir, 1 * 1024 * 1024)) // 1 MB
        .addInterceptor(OkHttpInterceptors.createOkHttpInterceptor())
        .addNetworkInterceptor(OkHttpInterceptors.createOkHttpNetworkInterceptor())
        .build()
}
