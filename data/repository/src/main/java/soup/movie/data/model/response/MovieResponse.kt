package soup.movie.data.model.response

import com.google.gson.annotations.SerializedName

data class MovieResponse(
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
    val theater: TheaterRatingsResponse
) {

    val posterUrl: String
        get() = _posterUrl.replaceFirst("http:", "https:")

    val isPlan: Boolean
        get() = !isNow
}
