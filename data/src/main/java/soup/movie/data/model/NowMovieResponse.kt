package soup.movie.data.model

import com.google.gson.annotations.SerializedName

data class NowMovieResponse(
        @SerializedName("lastUpdateTime") val lastUpdateTime: Int,
        @SerializedName("list") val list: List<Movie>
)