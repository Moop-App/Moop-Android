package soup.movie.data.api.response

import kotlinx.serialization.Serializable

/**
 * @param peopleNm 배우 (이름)
 * @param cast 배우 (역할)
 */
@Serializable
data class ActorResponse(
    val peopleNm: String,
    val cast: String
)
