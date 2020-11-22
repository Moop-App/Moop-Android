package soup.movie.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param genres 장르
 * @param nations 국가
 * @param companies 배급사/제작사
 * @param directors 감독
 * @param actors 배우
 * @param showTm 상영시간 (분)
 * @param boxOffice 박스오피스 정보
 */
@Serializable
data class MovieDetailResponse(
    val id: String,
    val score: Int,
    val title: String,
    @SerialName("posterUrl")
    private val _posterUrl: String,
    val openDate: String,
    @SerialName("now")
    val isNow: Boolean,
    val age: Int,
    val nationFilter: List<String>? = null,
    val genres: List<String>? = null,

    val boxOffice: BoxOfficeResponse? = null,
    val showTm: Int? = null,
    val nations: List<String>? = null,
    val directors: List<String>? = null,
    val actors: List<ActorResponse>? = null,
    val companies: List<CompanyResponse>? = null,
    val cgv: CgvInfoResponse? = null,
    val lotte: LotteInfoResponse? = null,
    val megabox: MegaboxInfoResponse? = null,
    val naver: NaverInfoResponse? = null,
    val imdb: ImdbInfoResponse? = null,
    val rt: RottenTomatoInfoResponse? = null,
    val mc: MetascoreInfoResponse? = null,
    val plot: String? = null,
    val trailers: List<TrailerResponse>? = null
) {

    val posterUrl: String
        get() = _posterUrl.replaceFirst("http:", "https:")

    val isPlan: Boolean
        get() = !isNow
}
