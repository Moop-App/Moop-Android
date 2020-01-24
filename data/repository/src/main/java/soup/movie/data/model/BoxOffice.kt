package soup.movie.data.model

/**
 * @param rank 전일 순위
 * @param audiCnt 전일 관객수
 * @param audiAcc 누적 관객수
 */
data class BoxOffice(
    val rank: Int,
    val audiCnt: Int,
    val audiAcc: Int
)
