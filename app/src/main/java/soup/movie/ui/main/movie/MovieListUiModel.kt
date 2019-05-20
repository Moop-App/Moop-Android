package soup.movie.ui.main.movie

import androidx.annotation.Keep
import soup.movie.data.model.Movie

sealed class MovieListUiModel {

    @Keep
    object LoadingState : MovieListUiModel()

    @Keep
    object ErrorState : MovieListUiModel()

    @Keep
    data class DoneState(
        val movies: List<Movie>
    ) : MovieListUiModel()

    fun hasNoItems(): Boolean {
        if (this is DoneState) {
            return movies.isEmpty()
        }
        return false
    }
}
