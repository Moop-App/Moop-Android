package soup.movie.data.model

import androidx.annotation.Keep

@Keep
data class Movie(
    val id: String,
    val title: String,
    val posterUrl: String,
    private val age: String,
    val ageValue: Int,
    val openDate: String,
    val cgv: CgvInfo?,
    val lotte: LotteInfo?,
    val megabox: MegaboxInfo?,
    val naver: NaverInfo?,
    val trailers: List<Trailer>?,
    val genre: List<String>?,
    val isNow: Boolean
) {

    val rank: Float
        get() {
            var rank = 0
            var count = 0
            if (cgv != null) {
                rank += cgv.rank
                count += 1
            }
            if (lotte != null) {
                rank += lotte.rank
                count += 1
            }
            if (megabox != null) {
                rank += megabox.rank
                count += 1
            }
            return rank.toFloat() / count
        }

    val isPlan: Boolean
        get() = !isNow

    fun toMovieId() = MovieId(
        id = id,
        title = title,
        cgvId = cgv?.id,
        lotteId = lotte?.id,
        megaboxId = megabox?.id
    )
}
