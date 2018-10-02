package soup.movie.data.model

data class Hall(
        val name: String,
        val format: String,
        val timeList: List<Time> = emptyList())
