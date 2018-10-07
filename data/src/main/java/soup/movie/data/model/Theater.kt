package soup.movie.data.model

data class Theater(
        val type: String,
        val code: String,
        val name: String,
        val lng: Double,
        val lat: Double) {

    val id: String
        get() = "$type:$code"

    companion object {

        const val TYPE_CGV = "C"
        const val TYPE_LOTTE = "L"
        const val TYPE_MEGABOX = "M"

        val NONE: Theater = Theater("", "", "", 0.0, 0.0)
    }
}
