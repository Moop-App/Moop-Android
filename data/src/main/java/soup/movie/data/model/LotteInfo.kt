package soup.movie.data.model

import androidx.annotation.Keep

@Keep
data class LotteInfo(
    val rank: Int,
    val id: String,
    val star: String,
    val specialTypes: List<String>?
)
