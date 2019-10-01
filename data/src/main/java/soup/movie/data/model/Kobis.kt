package soup.movie.data.model

import androidx.annotation.Keep

/**
 * @param genres 장르
 * @param nations 국가
 * @param companys 배급사/제작사
 * @param directors 감독
 * @param actors 배우
 * @param showTm 상영시간 (분)
 * @param boxOffice 박스오피스 정보
 */
@Keep
data class Kobis (
    val genres: List<String>?,
    val nations: List<String>?,
    val companys: List<Company>?,
    val directors: List<String>?,
    val actors: List<Actor>?,
    val showTm: Int,
    val boxOffice: BoxOffice?
)

/**
 * @param companyNm 회사 (이름)
 * @param companyPartNm 회사 (역할)
 */
@Keep
data class Company(
    val companyNm: String,
    val companyPartNm: String
)

/**
 * @param peopleNm 배우 (이름)
 * @param cast 배우 (역할)
 */
@Keep
data class Actor(
    val peopleNm: String,
    val cast: String
)

/**
 * @param rank 전일 순위
 * @param audiCnt 전일 관객수
 * @param audiAcc 누적 관객수
 */
@Keep
data class BoxOffice(
    val rank: Int,
    val audiCnt: Int,
    val audiAcc: Int
)
