package soup.movie.data.api.response

data class TheaterAreaResponse(
    val area: AreaResponse,
    val theaterList: List<TheaterResponse>
)
