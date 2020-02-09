package soup.movie.data.db.internal.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import soup.movie.data.db.internal.entity.MovieEntity

internal class CacheDatabaseTypeConverter {

    private val gson = Gson()
    private val typeMovieEntityList = object : TypeToken<List<MovieEntity>>() {}.type

    @TypeConverter
    fun toList(data: String?): List<MovieEntity> = when (data) {
        null -> emptyList()
        else -> gson.fromJson<List<MovieEntity>>(data, typeMovieEntityList)
    }

    @TypeConverter
    fun toString(movies: List<MovieEntity>): String {
        return gson.toJson(movies)
    }
}
