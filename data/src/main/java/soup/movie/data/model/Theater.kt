package soup.movie.data.model

typealias TheaterType = String

data class Theater(
        val type: TheaterType,
        val code: String,
        val name: String,
        val lng: Double,
        val lat: Double) {

    val id: String
        get() = "$type:$code"

    companion object {

        const val TYPE_CGV: TheaterType = "C"
        const val TYPE_LOTTE: TheaterType = "L"
        const val TYPE_MEGABOX: TheaterType = "M"
        const val TYPE_NONE: TheaterType = ""

        val NONE: Theater = Theater(TYPE_NONE, "", "", 0.0, 0.0)
    }
}
