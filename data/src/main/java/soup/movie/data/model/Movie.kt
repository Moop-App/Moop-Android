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
}
