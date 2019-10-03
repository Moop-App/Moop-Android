package soup.movie

import android.content.Context
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

object BuildType {

    fun init(context: Context) {
        Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(context)
    }

    fun addNetworkInterceptor(builder: Builder): Builder = builder
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .addNetworkInterceptor(StethoInterceptor())
}
