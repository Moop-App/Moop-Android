package soup.movie.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TheaterRatingsResponse(
    @SerialName("C")
    val cgv: String? = null,
    @SerialName("L")
    val lotte: String? = null,
    @SerialName("M")
    val megabox: String? = null
)
