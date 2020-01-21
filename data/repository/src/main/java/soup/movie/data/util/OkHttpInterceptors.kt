package soup.movie.data.util

import okhttp3.Interceptor
import okhttp3.Request

object OkHttpInterceptors {

    private const val HEADER_CACHE_CONTROL = "Cache-Control"
    private const val HEADER_CACHE_MAX_AGE = "public, max-age=${5 * 60}" // 5 minutes

    private const val HEADER_USE_CACHE_PREFIX = "Use-Cache"
    const val HEADER_USE_CACHE = "${HEADER_USE_CACHE_PREFIX}: "

    private fun Request.useCache(): Boolean {
        return header(HEADER_USE_CACHE_PREFIX) != null
    }

    fun provideOkHttpInterceptor(): Interceptor = Interceptor {
        it.proceed(it.request().newBuilder().build())
    }

    fun provideOkHttpNetworkInterceptor(): Interceptor = Interceptor {
        val request = it.request()
        if (request.useCache()) {
            it.proceed(request)
                .newBuilder()
                .header(HEADER_CACHE_CONTROL, HEADER_CACHE_MAX_AGE)
                .removeHeader(HEADER_USE_CACHE_PREFIX)
                .build()
        } else {
            it.proceed(request)
        }
    }
}
