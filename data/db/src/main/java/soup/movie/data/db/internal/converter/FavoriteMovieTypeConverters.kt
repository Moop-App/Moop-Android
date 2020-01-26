package soup.movie.data.db.internal.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object FavoriteMovieTypeConverters {

    private val gson = Gson()
    private val typeStringList = object : TypeToken<List<String>>() {}.type

    @TypeConverter
    @JvmStatic
    fun fromStringList(value: List<String>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun toStringList(value: String?): List<String>? {
        return gson.fromJson(value,
            typeStringList
        )
    }
}
