package soup.movie.util

import com.google.gson.Gson
import timber.log.Timber

inline fun <reified T : Any> T.toJson(): String = Gson().toJson(this, T::class.java)

inline fun <reified T : Any> String.fromJson(): T? = try {
    Gson().fromJson(this, T::class.java)
} catch (t: Throwable) {
    Timber.w(t)
    null
}
