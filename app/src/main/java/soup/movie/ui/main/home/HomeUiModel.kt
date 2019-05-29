package soup.movie.ui.main.home

import androidx.annotation.Keep
import soup.movie.data.model.Movie

sealed class HomeUiModel {

    @Keep
    object LoadingState : HomeUiModel()

    @Keep
    object ErrorState : HomeUiModel()

    @Keep
    data class DoneState(
        val movies: List<Movie>
    ) : HomeUiModel()

    fun hasNoItems(): Boolean {
        if (this is DoneState) {
            return movies.isEmpty()
        }
        return false
    }
}
