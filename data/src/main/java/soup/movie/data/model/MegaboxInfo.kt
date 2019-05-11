package soup.movie.data.model

import androidx.annotation.Keep

typealias MegaboxMovieId = String

@Keep
data class MegaboxInfo(
    val rank: Int,
    val id: MegaboxMovieId,
    val star: String,
    val specialTypes: List<String>?
)
