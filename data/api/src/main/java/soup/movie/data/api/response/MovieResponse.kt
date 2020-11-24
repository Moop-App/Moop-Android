package soup.movie.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    val id: String,
    val score: Int,
    val title: String,
    @SerialName("posterUrl")
    private val _posterUrl: String,
    val openDate: String,
    @SerialName("now")
    val isNow: Boolean,
    val age: Int,
    val nationFilter: List<String>? = null,
    val genres: List<String>? = null,
    val boxOffice: Int? = null,
    val theater: TheaterRatingsResponse
) {

    val posterUrl: String
        get() = _posterUrl.replaceFirst("http:", "https:")
}
