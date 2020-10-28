package soup.movie.data.api.response

import kotlinx.serialization.Serializable

@Serializable
data class CgvInfoResponse(
    val id: String,
    val star: String,
    val url: String?
)
