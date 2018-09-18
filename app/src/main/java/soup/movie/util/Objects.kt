package soup.movie.util

import com.google.gson.Gson

inline fun <reified T : Any> T.toJson(): String = Gson().toJson(this, T::class.java)

inline fun <reified T : Any> String.fromJson(): T? = try {
    Gson().fromJson(this, T::class.java)
} catch (t: Throwable) {
    null
}
