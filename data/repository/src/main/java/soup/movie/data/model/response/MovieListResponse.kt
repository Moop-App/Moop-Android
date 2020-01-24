package soup.movie.data.model.response

data class MovieListResponse(
    val lastUpdateTime: Long,
    val list: List<MovieResponse>
)
