package soup.movie.data.api.response

import kotlinx.serialization.Serializable

@Serializable
data class AreaResponse(
    val code: String,
    val name: String
)
