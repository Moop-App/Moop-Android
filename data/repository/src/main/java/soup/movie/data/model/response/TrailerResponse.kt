package soup.movie.data.model.response

import androidx.core.text.parseAsHtml
import com.google.gson.annotations.SerializedName

data class TrailerResponse(
    val youtubeId: String,
    @SerializedName("title")
    private val _title: String,
    val author: String,
    val thumbnailUrl: String
) {
    val title: String
        get() = _title.parseAsHtml().toString()
}
