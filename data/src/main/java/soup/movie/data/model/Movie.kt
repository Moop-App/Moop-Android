package soup.movie.data.model

data class Movie(
        val id: String,
        val title: String,
        val thumbnail: String,
        val poster: String,
        val age: String,
        val openDate: String,
        val egg: String,
        val specialTypes: List<String>,
        val trailers: List<Trailer>)