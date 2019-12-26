package soup.movie.data.model

import androidx.annotation.Keep

typealias CgvMovieId = String

@Keep
data class CgvInfo(
    val id: CgvMovieId,
    val star: String
)
