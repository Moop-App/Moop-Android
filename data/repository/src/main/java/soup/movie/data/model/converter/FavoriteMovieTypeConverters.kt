package soup.movie.data.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import soup.movie.data.model.*
import soup.movie.data.util.lazyFast

object FavoriteMovieTypeConverters {

    private val gson = Gson()

    private val typeStringList by lazyFast { object : TypeToken<List<String>>() {}.type }
    private val typeBoxOffice by lazyFast { object : TypeToken<BoxOffice>() {}.type }
    private val typeActorList by lazyFast { object : TypeToken<List<Actor>>() {}.type }
    private val typeCompanyList by lazyFast { object : TypeToken<List<Company>>() {}.type }
    private val typeNaverInfo by lazyFast { object : TypeToken<NaverInfo>() {}.type }
    private val typeImdbInfo by lazyFast { object : TypeToken<ImdbInfo>() {}.type }
    private val typeTrailerList by lazyFast { object : TypeToken<List<Trailer>>() {}.type }

    @TypeConverter
    @JvmStatic
    fun fromStringList(value: List<String>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun toStringList(value: String?): List<String>? {
        return gson.fromJson(value, typeStringList)
    }

    @TypeConverter
    @JvmStatic
    fun fromBoxOffice(value: BoxOffice?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun toBoxOffice(value: String?): BoxOffice? {
        return gson.fromJson(value, typeBoxOffice)
    }

    @TypeConverter
    @JvmStatic
    fun fromActorList(value: List<Actor>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun toActorList(value: String?): List<Actor>? {
        return gson.fromJson(value, typeActorList)
    }

    @TypeConverter
    @JvmStatic
    fun fromCompanyList(value: List<Company>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun toCompanyList(value: String?): List<Company>? {
        return gson.fromJson(value, typeCompanyList)
    }

    @TypeConverter
    @JvmStatic
    fun fromNaverInfo(value: NaverInfo?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun toNaverInfo(value: String?): NaverInfo? {
        return gson.fromJson(value, typeNaverInfo)
    }

    @TypeConverter
    @JvmStatic
    fun fromImdbInfo(value: ImdbInfo?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun toImdbInfo(value: String?): ImdbInfo? {
        return gson.fromJson(value, typeImdbInfo)
    }

    @TypeConverter
    @JvmStatic
    fun fromTrailerList(value: List<Trailer>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun toTrailerList(value: String?): List<Trailer>? {
        return gson.fromJson(value, typeTrailerList)
    }
}
