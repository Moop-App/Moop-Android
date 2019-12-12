package soup.movie.data.model.response

import soup.movie.data.model.Movie
import soup.movie.data.model.response.CachedMovieList.Companion.STALE_MS

data class MovieListResponse(
    val lastUpdateTime: Long,
    val list: List<Movie>
)

fun MovieListResponse.isStaleness(): Boolean {
    return (list.isEmpty() || System.currentTimeMillis() - lastUpdateTime > STALE_MS)
}
