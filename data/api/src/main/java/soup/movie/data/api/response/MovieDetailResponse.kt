package soup.movie.data.api.response

import com.google.gson.annotations.SerializedName

/**
 * @param genres 장르
 * @param nations 국가
 * @param companies 배급사/제작사
 * @param directors 감독
 * @param actors 배우
 * @param showTm 상영시간 (분)
 * @param boxOffice 박스오피스 정보
 */
data class MovieDetailResponse(
    val id: String,
    val score: Int,
    val title: String,
    @SerializedName("posterUrl")
    private val _posterUrl: String,
    val openDate: String,
    @SerializedName("now")
    val isNow: Boolean,
    val age: Int,
    val nationFilter: List<String>?,
    val genres: List<String>?,

    val boxOffice: BoxOfficeResponse?,
    val showTm: Int?,
    val nations: List<String>?,
    val directors: List<String>?,
    val actors: List<ActorResponse>?,
    val companies: List<CompanyResponse>?,
    val cgv: CgvInfoResponse?,
    val lotte: LotteInfoResponse?,
    val megabox: MegaboxInfoResponse?,
    val naver: NaverInfoResponse?,
    val imdb: ImdbInfoResponse?,
    val rt: RottenTomatoInfoResponse?,
    val mc: MetascoreInfoResponse?,
    val plot: String?,
    val trailers: List<TrailerResponse>?
) {

    val posterUrl: String
        get() = _posterUrl.replaceFirst("http:", "https:")

    val isPlan: Boolean
        get() = !isNow
}
