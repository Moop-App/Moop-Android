package soup.movie.util

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import soup.movie.data.model.Movie

private const val KEY_JSON = "json"

fun Bundle.restoreFrom(): Movie? = getString(KEY_JSON)?.fromJson()

fun Intent.restoreFrom(): Movie? = getStringExtra(KEY_JSON).fromJson()

fun Movie.saveTo(bundle: Bundle) = bundle.putString(KEY_JSON, toJson())

fun Movie.saveTo(intent: Intent): Intent = intent.putExtra(KEY_JSON, toJson())

private fun Movie.toJson(): String = Gson().toJson(this)

private fun String.fromJson(): Movie? = Gson().fromJson(this, Movie::class.java)

fun Movie.toShareDescription(): String = "제목: $title\n개봉일: $openDate\n연령제한: $age"
