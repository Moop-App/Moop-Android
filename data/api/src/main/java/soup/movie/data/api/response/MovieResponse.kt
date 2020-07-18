package soup.movie.data.api.response

import com.squareup.moshi.Json

data class MovieResponse(
    val id: String,
    val score: Int,
    val title: String,
    @Json(name = "posterUrl")
    private val _posterUrl: String,
    val openDate: String,
    @Json(name = "now")
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
