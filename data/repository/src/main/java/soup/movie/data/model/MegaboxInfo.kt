package soup.movie.data.model

import androidx.annotation.Keep

typealias MegaboxMovieId = String

@Keep
data class MegaboxInfo(
    val id: MegaboxMovieId,
    val star: String
)
