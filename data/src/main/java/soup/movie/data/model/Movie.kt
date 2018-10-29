package soup.movie.data.model

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
        val isNow: Boolean) {

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

    val cgvEgg: String
        get() = cgv?.egg ?: "-"

    val lotteStar: String
        get() = lotte?.star ?: "-"

    val megaboxStar: String
        get() = megabox?.star ?: "-"

    val naverUserRating: String
        get() = naver?.userRating ?: "-"

    fun isScreeningAtCgv(): Boolean = cgv != null
    fun isScreeningAtLotteCinema(): Boolean = lotte != null
    fun isScreeningAtMegabox(): Boolean = megabox != null
    fun hasNaverInfo(): Boolean = naver != null

    fun toMovieId() = MovieId(
            id = id,
            title = title,
            cgvId = cgv?.id,
            lotteId = lotte?.id,
            megaboxId = megabox?.id)

    fun isMatchedWith(movieId: MovieId): Boolean {
        return id == movieId.id
            || title == movieId.title
            || cgv?.id.isMatched(movieId.cgvId)
            || lotte?.id.isMatched(movieId.lotteId)
            || megabox?.id.isMatched(movieId.megaboxId)
    }

    private fun String?.isMatched(id: String?): Boolean {
        if (this == null || id == null) return false
        return this == id
    }
}
