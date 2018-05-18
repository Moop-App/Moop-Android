package soup.movie.data.model

import com.google.gson.annotations.SerializedName

data class Trailer(
        @SerializedName("author") val author: String,
        @SerializedName("title") val title: String,
        @SerializedName("youtube_id") val youtubeId: String,
        @SerializedName("thumbnails") val thumbnails: Thumbnails
)