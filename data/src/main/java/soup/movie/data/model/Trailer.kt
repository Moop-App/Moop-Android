package soup.movie.data.model

import androidx.annotation.Keep

@Keep
data class Trailer(
    val youtubeId: String,
    val title: String,
    val author: String,
    val thumbnailUrl: String
)
