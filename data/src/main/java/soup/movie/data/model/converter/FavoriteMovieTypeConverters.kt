package soup.movie.data.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoriteMovieTypeConverters {

    private val gson = Gson()

    @TypeConverter
    fun toList(json: String?): List<String>? {
        return gson.fromJson(json, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    fun toString(list: List<String>?): String {
        return gson.toJson(list)
    }
}
