package soup.movie.data.model

import androidx.annotation.Keep

typealias CgvMovieId = String

@Keep
data class CgvInfo(
    val rank: Int,
    val id: CgvMovieId,
    val egg: String,
    val specialTypes: List<String>?
)
