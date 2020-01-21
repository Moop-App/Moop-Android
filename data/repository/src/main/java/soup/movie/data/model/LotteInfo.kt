package soup.movie.data.model

import androidx.annotation.Keep

typealias LotteMovieId = String

@Keep
data class LotteInfo(
    val id: LotteMovieId,
    val star: String
)
