package soup.movie.data.db.internal.converter

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import soup.movie.data.db.internal.entity.MovieEntity

internal class CacheDatabaseTypeConverter {

    @TypeConverter
    fun fromString(string: String?): List<MovieEntity> {
        if (string == null) {
            return emptyList()
        }
        return Json.decodeFromString(string)
    }

    @TypeConverter
    fun toString(movies: List<MovieEntity>): String {
        return Json.encodeToString(movies)
    }
}
