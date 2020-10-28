package soup.movie.data.api.response

import kotlinx.serialization.Serializable

@Serializable
data class NaverInfoResponse(
    val star: String,
    val url: String?
)
