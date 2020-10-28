package soup.movie.data.api.response

import kotlinx.serialization.Serializable

@Serializable
data class RottenTomatoInfoResponse(
    val star: String?
)
