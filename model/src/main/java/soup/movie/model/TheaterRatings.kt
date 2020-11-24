package soup.movie.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TheaterRatings(
    val cgv: String?,
    val lotte: String?,
    val megabox: String?
) : Parcelable
