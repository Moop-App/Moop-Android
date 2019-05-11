package soup.movie.data.model

import androidx.annotation.Keep

@Keep
data class MegaboxInfo(
    val rank: Int,
    val id: String,
    val star: String,
    val specialTypes: List<String>?
)
