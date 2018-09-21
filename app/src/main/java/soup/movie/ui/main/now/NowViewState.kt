package soup.movie.ui.main.now

import androidx.annotation.Keep
import soup.movie.data.model.Movie

sealed class NowViewState {

    @Keep
    object LoadingState : NowViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object ErrorState : NowViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    data class DoneState(val movies: List<Movie>) : NowViewState()
}
