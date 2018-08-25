package soup.movie.data.response

import soup.movie.data.model.Movie

data class NowMovieResponse(
        val lastUpdateTime: Int,
        val list: List<Movie>)
