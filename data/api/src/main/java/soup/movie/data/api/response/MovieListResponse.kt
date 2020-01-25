package soup.movie.data.api.response

data class MovieListResponse(
    val lastUpdateTime: Long,
    val list: List<MovieResponse>
)
