package soup.movie.data.model.response

data class TheaterAreaGroupResponse(
    val lastUpdateTime: Long,
    val cgv: List<TheaterAreaResponse>,
    val lotte: List<TheaterAreaResponse>,
    val megabox: List<TheaterAreaResponse>
)
