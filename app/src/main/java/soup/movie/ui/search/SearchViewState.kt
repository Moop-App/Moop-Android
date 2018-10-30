package soup.movie.ui.search

import androidx.annotation.Keep
import soup.movie.data.model.Movie

sealed class SearchViewState {

    @Keep
    object LoadingState : SearchViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object ErrorState : SearchViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    data class DoneState(val items: List<Movie>) : SearchViewState()

    fun hasNoItems(): Boolean {
        if (this is DoneState) {
            return items.isEmpty()
        }
        return false
    }
}
