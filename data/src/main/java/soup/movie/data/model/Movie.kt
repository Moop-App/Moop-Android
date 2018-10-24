package soup.movie.data.model

data class Movie(
        val id: String,
        val title: String,
        val posterUrl: String,
        val age: String,
        val openDate: String,
        val cgv: CgvInfo?,
        val lotte: LotteInfo?,
        val megabox: MegaboxInfo?,
        val trailers: List<Trailer>?,
        val isNow: Boolean) {

    val isPlan: Boolean
            get() = !isNow

    val cgvEgg: String
        get() = cgv?.egg ?: "-"

    val lotteStar: String
        get() = lotte?.star ?: "-"

    val megaboxStar: String
        get() = megabox?.star ?: "-"

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
