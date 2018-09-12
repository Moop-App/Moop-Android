package soup.movie.data.model

typealias TheaterId = String

data class Theater(
        val type: TheaterId,
        val code: String,
        val name: String,
        //TODO: Change to Double
        val lng: String?,
        val lat: String?) {

    val isCgv: Boolean = type == "C"
    val isLotteCinema: Boolean = type == "L"
    val isMegabox: Boolean = type == "M"
}
