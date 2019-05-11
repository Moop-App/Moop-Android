package soup.movie.data.model

import androidx.annotation.Keep

@Keep
data class MovieId(
    val id: String,
    val title: String,
    val cgvId: String?,
    val lotteId: String?,
    val megaboxId: String?
)
