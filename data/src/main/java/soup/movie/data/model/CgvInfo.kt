package soup.movie.data.model

import androidx.annotation.Keep

@Keep
data class CgvInfo(
    val rank: Int,
    val id: String,
    val egg: String,
    val specialTypes: List<String>?
)
