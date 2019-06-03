package soup.movie.settings.model

class GenreFilter(
    val blacklist: Set<String>
) {

    companion object {

        const val GENRE_ETC = "장르 없음"
    }
}
