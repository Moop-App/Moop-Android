package soup.movie.util

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import soup.movie.data.model.Movie

object MovieUtil {

    private val KEY_JSON = "json"

    @JvmStatic
    fun restoreFrom(bundle: Bundle): Movie?
            = fromJson(bundle.getString(KEY_JSON))

    @JvmStatic
    fun restoreFrom(intent: Intent): Movie?
            = fromJson(intent.getStringExtra(KEY_JSON))

    @JvmStatic
    fun saveTo(bundle: Bundle, movie: Movie) {
        bundle.putString(KEY_JSON, toJson(movie))
    }

    @JvmStatic
    fun saveTo(intent: Intent, movie: Movie) {
        intent.putExtra(KEY_JSON, toJson(movie))
    }

    private fun toJson(movie: Movie): String =
            Gson().toJson(movie)

    private fun fromJson(jsonStr: String?): Movie? =
            Gson().fromJson(jsonStr, Movie::class.java)

    @JvmStatic
    fun createShareDescription(movie: Movie): String =
            "제목: ${movie.title}\n개봉일: ${movie.openDate}\n연령제한: ${movie.age}"
}
