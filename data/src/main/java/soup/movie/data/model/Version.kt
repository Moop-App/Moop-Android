package soup.movie.data.model

import androidx.annotation.Keep

@Keep
data class Version(
    val versionCode: Int,
    val versionName: String
)
