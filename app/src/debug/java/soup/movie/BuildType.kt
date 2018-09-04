package soup.movie

import android.content.Context
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import soup.movie.util.log.CrashlyticsTree
import timber.log.Timber

object BuildType {

    fun init(context: Context) {
        Timber.plant(Timber.DebugTree())
        Timber.plant(CrashlyticsTree())
        Stetho.initializeWithDefaults(context)
    }

    fun addNetworkInterceptor(builder: Builder): Builder = builder
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(StethoInterceptor())
}
