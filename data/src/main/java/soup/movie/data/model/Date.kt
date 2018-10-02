package soup.movie.data.model

data class Date(
        val date: String,
        val hallList: List<Hall> = emptyList()) {

    val year: Int
        get() = date.substring(0..3).toInt()

    val month: Int
        get() = date.substring(4..5).toInt()

    val day: Int
        get() = date.substring(6..7).toInt()
}
