package soup.movie.data.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import soup.movie.data.model.Movie

class MovieListTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun toList(data: String?): List<Movie> = when (data) {
        null -> emptyList()
        else -> gson.fromJson<List<Movie>>(data, object : TypeToken<List<Movie>>() {}.type)
    }

    @TypeConverter
    fun toString(movies: List<Movie>): String {
        return gson.toJson(movies)
    }
}
