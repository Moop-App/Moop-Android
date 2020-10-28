package soup.movie.data.api.response

import kotlinx.serialization.Serializable

@Serializable
data class MovieListResponse(
    val lastUpdateTime: Long,
    val list: List<MovieResponse>
)
