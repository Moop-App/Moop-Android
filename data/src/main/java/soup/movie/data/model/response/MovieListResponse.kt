package soup.movie.data.model.response

import soup.movie.data.model.Movie

data class MovieListResponse(
        val lastUpdateTime: Long,
        val list: List<Movie>)
