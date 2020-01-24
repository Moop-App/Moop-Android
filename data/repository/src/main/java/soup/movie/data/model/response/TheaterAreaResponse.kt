package soup.movie.data.model.response

data class TheaterAreaResponse(
    val area: AreaResponse,
    val theaterList: List<TheaterResponse>
)
