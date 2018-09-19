package soup.movie.ui.main.now

import soup.movie.data.model.Movie

sealed class NowViewState {

    object LoadingState : NowViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    object ErrorState : NowViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    data class DoneState(val movies: List<Movie>) : NowViewState()
}
