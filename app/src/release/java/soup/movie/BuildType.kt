package soup.movie

import android.content.Context

import okhttp3.OkHttpClient.Builder

object BuildType {

    fun init(context: Context) {}

    fun addNetworkInterceptor(builder: Builder): Builder = builder
}
