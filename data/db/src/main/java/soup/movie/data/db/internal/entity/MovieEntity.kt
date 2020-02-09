package soup.movie.data.db.internal.entity

internal data class MovieEntity(
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
    val cgv: String?,
    val lotte: String?,
    val megabox: String?
)
