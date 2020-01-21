package soup.movie.data.model

import androidx.annotation.Keep

typealias TheaterType = String

@Keep
data class Theater(
    val type: TheaterType,
    val code: String,
    val name: String,
    val lng: Double,
    val lat: Double
) {

    val id: String
        get() = "$type:$code"

    companion object {

        const val TYPE_CGV: TheaterType = "C"
        const val TYPE_LOTTE: TheaterType = "L"
        const val TYPE_MEGABOX: TheaterType = "M"
    }
}
