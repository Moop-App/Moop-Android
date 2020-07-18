package soup.movie.data.api.response

import androidx.core.text.parseAsHtml
import com.squareup.moshi.Json

data class TrailerResponse(
    val youtubeId: String,
    @Json(name = "title")
    private val _title: String,
    val author: String,
    val thumbnailUrl: String
) {

    val title: String
        get() = _title.parseAsHtml().toString()
}
