package soup.movie.data.api.response

import kotlinx.serialization.Serializable

@Serializable
data class TheaterAreaResponse(
    val area: AreaResponse,
    val theaterList: List<TheaterResponse>
)
