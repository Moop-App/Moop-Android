package soup.movie.data.model

import androidx.annotation.Keep

@Keep
data class ImdbInfo(
    val id: String,
    val star: String,
    val url: String?
)

