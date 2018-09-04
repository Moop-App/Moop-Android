package soup.movie

import android.content.Context
import okhttp3.OkHttpClient.Builder
import soup.movie.util.log.CrashlyticsTree
import timber.log.Timber

object BuildType {

    fun init(context: Context) {
        Timber.plant(CrashlyticsTree())
    }

    fun addNetworkInterceptor(builder: Builder): Builder = builder
}
