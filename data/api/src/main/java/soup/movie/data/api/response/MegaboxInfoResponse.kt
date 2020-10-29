package soup.movie.data.api.response

import kotlinx.serialization.Serializable

@Serializable
data class MegaboxInfoResponse(
    val id: String,
    val star: String,
    val url: String? = null
)
