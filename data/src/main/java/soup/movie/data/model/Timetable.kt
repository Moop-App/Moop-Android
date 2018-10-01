package soup.movie.data.model

data class Timetable(
        val theater: Theater = Theater.NONE,
        val dateList: List<Date> = emptyList())
