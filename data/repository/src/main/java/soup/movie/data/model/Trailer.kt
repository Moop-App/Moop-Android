package soup.movie.data.model

import androidx.annotation.Keep
import androidx.core.text.HtmlCompat
import com.google.gson.annotations.SerializedName

@Keep
data class Trailer(
    val youtubeId: String,
    @SerializedName("title")
    private val _title: String,
    val author: String,
    val thumbnailUrl: String
) {
    val title: String
        get() = HtmlCompat.fromHtml(_title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
}
