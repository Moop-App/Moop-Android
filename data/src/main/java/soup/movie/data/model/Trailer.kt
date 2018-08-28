package soup.movie.data.model

import com.google.gson.annotations.SerializedName

data class Trailer(
        val author: String,
        val title: String,
        @SerializedName("youtube_id")
        val youtubeId: String,
        val thumbnails: ThumbnailGroup) {

    fun getThumbnailUrl(): String {
        return thumbnails.high.url
                ?: thumbnails.medium.url
                ?: thumbnails.default.url
                ?: getThumbnailUrl(youtubeId)
    }

    private fun getThumbnailUrl(id: String, quality: Quality = Quality.STANDARD): String {
        return String.format("https://img.youtube.com/vi/%s/%s.jpg", id, quality.key)
    }

    enum class Quality(val key: String) {
        LOW("default"),
        MEDIUM("mqdefault"),
        HIGH("hqdefault"),
        STANDARD("sddefault"),
        MAX("maxresdefault");
    }
}
