package soup.movie.data.api.response

import androidx.core.text.parseAsHtml
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrailerResponse(
    val youtubeId: String,
    @SerialName("title")
    private val _title: String,
    val author: String = "",
    val thumbnailUrl: String? = null
) {

    val title: String
        get() = _title.parseAsHtml().toString()
}
