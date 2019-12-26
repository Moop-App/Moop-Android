package soup.movie.data.model

import androidx.annotation.Keep

typealias ImdbMovieId = String

@Keep
data class ImdbInfo(
    val id: ImdbMovieId,
    val star: String,
    val url: Url?
)

@Keep
data class MetascoreInfo(
    val star: String?
)

@Keep
data class RottenInfo(
    val star: String?
)
