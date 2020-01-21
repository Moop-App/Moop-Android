package soup.movie.data.model

import androidx.annotation.Keep

@Keep
data class NaverInfo(
    val star: String,
    val url: Url?
)
