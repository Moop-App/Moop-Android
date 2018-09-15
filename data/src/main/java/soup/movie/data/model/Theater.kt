package soup.movie.data.model

typealias TheaterId = String

data class Theater(
        val type: TheaterId,
        val code: String,
        val name: String,
        val lng: Double,
        val lat: Double) {

    companion object {

        const val TYPE_CGV = "C"
        const val TYPE_LOTTE = "L"
        const val TYPE_MEGABOX = "M"
    }
}
