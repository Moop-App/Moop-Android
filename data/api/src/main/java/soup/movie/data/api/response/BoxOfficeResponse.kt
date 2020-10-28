package soup.movie.data.api.response

import kotlinx.serialization.Serializable

/**
 * @param rank 전일 순위
 * @param audiCnt 전일 관객수
 * @param audiAcc 누적 관객수
 */
@Serializable
data class BoxOfficeResponse(
    val rank: Int,
    val audiCnt: Int,
    val audiAcc: Int
)
