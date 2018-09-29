package soup.movie.data.model

data class Date(
    val date: String,
    val timeList: List<String>? = emptyList()) {

    val year: String
        get() = date.substring(0..3)

    val month: String
        get() = date.substring(4..5)

    val day: String
        get() = date.substring(6..7)
}
