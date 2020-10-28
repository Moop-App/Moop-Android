package soup.movie.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TheaterRatingsResponse(
    @SerialName("C")
    val cgv: String?,
    @SerialName("L")
    val lotte: String?,
    @SerialName("M")
    val megabox: String?
)
