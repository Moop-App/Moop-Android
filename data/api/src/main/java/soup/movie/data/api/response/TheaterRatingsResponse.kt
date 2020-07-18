package soup.movie.data.api.response

import com.squareup.moshi.Json

data class TheaterRatingsResponse(
    @Json(name = "C")
    val cgv: String?,
    @Json(name = "L")
    val lotte: String?,
    @Json(name = "M")
    val megabox: String?
)
