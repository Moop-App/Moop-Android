package soup.movie.ui.main.movie

import androidx.annotation.Keep
import soup.movie.data.model.Movie

sealed class MovieListViewState {

    @Keep
    object LoadingState : MovieListViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object ErrorState : MovieListViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    data class DoneState(val movies: List<Movie>) : MovieListViewState()

    fun hasItems(): Boolean {
        if (this is DoneState) {
            return movies.isNotEmpty()
        }
        return false
    }
}
