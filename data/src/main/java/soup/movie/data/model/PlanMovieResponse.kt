package soup.movie.data.model

import com.google.gson.annotations.SerializedName

data class PlanMovieResponse(
        @SerializedName("lastUpdateTime") val lastUpdateTime: Int,
        @SerializedName("list") val list: List<Movie>
)