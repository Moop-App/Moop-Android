package soup.movie.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Movie(
    val id: String,
    val score: Int,
    val title: String,
    @SerializedName("posterUrl")
    private val _posterUrl: String,
    val openDate: String,
    @SerializedName("now")
    val isNow: Boolean,
    val age: Int,
    val nationFilter: List<String>?,
    val genres: List<String>?,
    val boxOffice: Int?,
    val theater: MovieTheater
) {

    val posterUrl: String
        get() = _posterUrl.replaceFirst("http:", "https:")

    val isPlan: Boolean
        get() = !isNow
}

@Keep
data class MovieTheater(
    @SerializedName("C")
    val cgv: String?,
    @SerializedName("L")
    val lotte: String?,
    @SerializedName("M")
    val megabox: String?
)
