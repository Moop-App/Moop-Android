package soup.movie.data.model

import com.google.gson.annotations.SerializedName

data class Trailer(
        val author: String,
        val title: String,
        @SerializedName("youtube_id")
        val youtubeId: String,
        val thumbnails: ThumbnailGroup)
