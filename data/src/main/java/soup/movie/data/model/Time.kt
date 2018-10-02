package soup.movie.data.model

data class Time(
        val start: String,
        val end: String,
        val remainSeat: Int,
        val totalSeat: Int) {

    fun start(): String = start.substring(0, 2) + ':' + start.substring(2, 4)
    fun end(): String = '~' + end.substring(0, 2) + ':' + end.substring(2, 4)
    fun seat(): String = "$remainSeat / $totalSeat"
}
