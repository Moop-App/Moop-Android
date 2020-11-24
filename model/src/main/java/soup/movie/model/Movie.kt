package soup.movie.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: String,
    val score: Int,
    val title: String,
    val posterUrl: String,
    val openDate: String,
    val isNow: Boolean,
    val age: Int,
    val nationFilter: List<String>?,
    val genres: List<String>?,
    val boxOffice: Int?,
    val theater: TheaterRatings
) : Parcelable {

    @IgnoredOnParcel
    val isPlan: Boolean = !isNow
}
