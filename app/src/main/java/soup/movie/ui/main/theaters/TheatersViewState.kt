package soup.movie.ui.main.theaters

import soup.movie.data.model.Theater

sealed class TheatersViewState {

    object LoadingState : TheatersViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    object ErrorState : TheatersViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    data class DoneState(val myTheaters: List<Theater>) : TheatersViewState()
}
