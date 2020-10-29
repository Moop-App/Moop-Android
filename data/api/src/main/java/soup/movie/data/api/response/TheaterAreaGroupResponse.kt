package soup.movie.data.api.response

import kotlinx.serialization.Serializable

@Serializable
data class TheaterAreaGroupResponse(
    val lastUpdateTime: Long,
    val cgv: List<TheaterAreaResponse> = emptyList(),
    val lotte: List<TheaterAreaResponse> = emptyList(),
    val megabox: List<TheaterAreaResponse> = emptyList()
)
