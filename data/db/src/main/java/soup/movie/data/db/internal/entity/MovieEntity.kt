package soup.movie.data.db.internal.entity

import kotlinx.serialization.Serializable

@Serializable
internal data class MovieEntity(
    val id: String,
    val score: Int,
    val title: String,
    val posterUrl: String,
    val openDate: String,
    val isNow: Boolean,
    val age: Int,
    val nationFilter: List<String>? = null,
    val genres: List<String>? = null,
    val boxOffice: Int? = null,
    val cgv: String? = null,
    val lotte: String? = null,
    val megabox: String? = null
)
