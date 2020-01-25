package soup.movie.data.api.response

import com.google.gson.annotations.SerializedName

data class TheaterRatingsResponse(
    @SerializedName("C")
    val cgv: String?,
    @SerializedName("L")
    val lotte: String?,
    @SerializedName("M")
    val megabox: String?
)
