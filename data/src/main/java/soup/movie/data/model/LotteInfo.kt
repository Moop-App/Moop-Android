package soup.movie.data.model

import androidx.annotation.Keep

typealias LotteMovieId = String

@Keep
data class LotteInfo(
    val rank: Int,
    val id: LotteMovieId,
    val star: String,
    val specialTypes: List<String>?
)
