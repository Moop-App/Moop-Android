package soup.movie.data.model

typealias TheaterId = String

data class Theater(
        val type: TheaterId,
        val code: String,
        val name: String,
        //TODO: Change to Double
        val lng: String?,
        val lat: String?) {

    companion object {

        const val TYPE_CGV = "C"
        const val TYPE_LOTTE = "L"
        const val TYPE_MEGABOX = "M"
    }
}
