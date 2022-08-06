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
package soup.movie.data.network.internal

import okhttp3.Interceptor
import okhttp3.Request

internal object OkHttpInterceptors {

    private const val HEADER_CACHE_CONTROL = "Cache-Control"
    private const val HEADER_CACHE_MAX_AGE = "public, max-age=${5 * 60}" // 5 minutes

    private const val HEADER_USE_CACHE_PREFIX = "Use-Cache"
    const val HEADER_USE_CACHE = "$HEADER_USE_CACHE_PREFIX: "

    private fun Request.useCache(): Boolean {
        return header(HEADER_USE_CACHE_PREFIX) != null
    }

    fun createOkHttpInterceptor() = Interceptor {
        it.proceed(it.request().newBuilder().build())
    }

    fun createOkHttpNetworkInterceptor() = Interceptor {
        val request = it.request()
        val useCache = request.useCache()
        it.proceed(request).apply {
            if (useCache) {
                newBuilder()
                    .header(HEADER_CACHE_CONTROL, HEADER_CACHE_MAX_AGE)
                    .removeHeader(HEADER_USE_CACHE_PREFIX)
                    .build()
            }
        }
    }
}
