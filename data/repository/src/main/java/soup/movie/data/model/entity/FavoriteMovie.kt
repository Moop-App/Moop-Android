package soup.movie.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import soup.movie.data.model.*

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "posterUrl")
    val posterUrl: String,
    @ColumnInfo(name = "openDate")
    val openDate: String,
    @ColumnInfo(name = "isNow")
    val isNow: Boolean,
    @ColumnInfo(name = "age")
    val age: Int,
    @ColumnInfo(name = "nationFilter")
    val nationFilter: List<String>?,
    @ColumnInfo(name = "genres")
    val genres: List<String>?,

    @ColumnInfo(name = "boxOffice")
    val boxOffice: BoxOffice?,
    @ColumnInfo(name = "showTm")
    val showTm: Int?,
    @ColumnInfo(name = "nations")
    val nations: List<String>?,
    @ColumnInfo(name = "directors")
    val directors: List<String>?,
    @ColumnInfo(name = "actors")
    val actors: List<Actor>?,
    @ColumnInfo(name = "companies")
    val companies: List<Company>?,
    @ColumnInfo(name = "cgv")
    val cgv: String?,
    @ColumnInfo(name = "lotte")
    val lotte: String?,
    @ColumnInfo(name = "megabox")
    val megabox: String?,
    @ColumnInfo(name = "naver")
    val naver: NaverInfo?,
    @ColumnInfo(name = "imdb")
    val imdb: ImdbInfo?,
    @ColumnInfo(name = "rt_star")
    val rt: String?,
    @ColumnInfo(name = "mc_star")
    val mc: String?,
    @ColumnInfo(name = "plot")
    val plot: String?,
    @ColumnInfo(name = "trailers")
    val trailers: List<Trailer>?
)
