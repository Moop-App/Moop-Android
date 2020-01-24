package soup.movie.data.model

data class TheaterAreaGroup(
    val lastUpdateTime: Long,
    val cgv: List<TheaterArea>,
    val lotte: List<TheaterArea>,
    val megabox: List<TheaterArea>
)
