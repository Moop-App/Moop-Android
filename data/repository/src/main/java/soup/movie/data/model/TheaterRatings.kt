package soup.movie.data.model

import com.google.gson.annotations.SerializedName

data class TheaterRatings(
    @SerializedName("C")
    val cgv: String?,
    @SerializedName("L")
    val lotte: String?,
    @SerializedName("M")
    val megabox: String?
)
