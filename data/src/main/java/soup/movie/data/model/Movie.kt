package soup.movie.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Movie(
    val id: String,
    @SerializedName("moopId")
    val moopId: String,
    val title: String,
    @SerializedName("posterUrl")
    private val _posterUrl: String,
    val age: String,
    val ageValue: Int,
    val openDate: String,
    val cgv: CgvInfo?,
    val lotte: LotteInfo?,
    val megabox: MegaboxInfo?,
    val naver: NaverInfo?,
    val trailers: List<Trailer>?,
    val genre: List<String>?,
    val plot: String?,
    val kobis: Kobis?,
    val imdb: ImdbInfo?,
    val isNow: Boolean
) {

    val posterUrl: String
        get() = _posterUrl.replaceFirst("http:", "https:")

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
