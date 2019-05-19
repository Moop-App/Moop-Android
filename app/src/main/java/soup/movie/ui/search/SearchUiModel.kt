package soup.movie.ui.search

import androidx.annotation.Keep
import soup.movie.data.model.Movie

sealed class SearchUiModel {

    @Keep
    object LoadingState : SearchUiModel() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object ErrorState : SearchUiModel() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    data class DoneState(val items: List<Movie>) : SearchUiModel()

    fun hasNoItems(): Boolean {
        if (this is DoneState) {
            return items.isEmpty()
        }
        return false
    }
}
