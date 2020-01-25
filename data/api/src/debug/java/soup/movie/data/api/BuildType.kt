package soup.movie.data.api

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor

fun Builder.addNetworkDebuggingInterceptor(): Builder = this.apply {
    addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
    addNetworkInterceptor(StethoInterceptor())
}
