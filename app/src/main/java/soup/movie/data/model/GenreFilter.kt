package soup.movie.data.model

data class GenreFilter(
    val blacklist: Set<String>
) {

    fun isEtcIncluded(): Boolean {
        return blacklist.contains(GENRE_ETC).not()
    }

    companion object {

        const val GENRE_ETC = "장르 없음"
    }
}
