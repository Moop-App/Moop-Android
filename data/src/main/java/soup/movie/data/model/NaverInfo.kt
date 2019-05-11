package soup.movie.data.model

import androidx.annotation.Keep

@Keep
data class NaverInfo(
    val title: String,
    val subtitle: String,
    val link: Url?,
    val userRating: String
)
