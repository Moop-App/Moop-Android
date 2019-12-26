package soup.movie.data.model

import androidx.annotation.Keep

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
