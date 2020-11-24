package soup.movie.model

/**
 * @param genres 장르
 * @param nations 국가
 * @param companies 배급사/제작사
 * @param directors 감독
 * @param actors 배우
 * @param showTm 상영시간 (분)
 * @param boxOffice 박스오피스 정보
 */
data class MovieDetail(
    val id: String,
    val score: Int,
    val title: String,
    val posterUrl: String,
    val openDate: String,
    val isNow: Boolean,
    val age: Int,
    val nationFilter: List<String>?,
    val genres: List<String>?,

    val boxOffice: BoxOffice?,
    val showTm: Int?,
    val nations: List<String>?,
    val directors: List<String>?,
    val actors: List<Actor>?,
    val companies: List<Company>?,
    val cgv: CgvInfo?,
    val lotte: LotteInfo?,
    val megabox: MegaboxInfo?,
    val naver: NaverInfo?,
    val imdb: ImdbInfo?,
    val rt: RottenTomatoInfo?,
    val mc: MetascoreInfo?,
    val plot: String?,
    val trailers: List<Trailer>?
)
