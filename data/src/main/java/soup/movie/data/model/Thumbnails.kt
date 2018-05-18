package soup.movie.data.model

import com.google.gson.annotations.SerializedName

data class Thumbnails(
        @SerializedName("default") val default: Thumbnail,
        @SerializedName("high") val high: Thumbnail,
        @SerializedName("medium") val medium: Thumbnail
)