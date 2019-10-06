package soup.movie.data.model

import androidx.annotation.Keep

typealias ImdbMovieId = String

@Keep
data class ImdbInfo(
    val id: ImdbMovieId,
    val imdb: String,
    val imdbUrl: Url?,
    val mc: String?,
    val rt: String?
)
