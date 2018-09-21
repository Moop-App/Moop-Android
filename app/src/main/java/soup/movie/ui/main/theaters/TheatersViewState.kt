package soup.movie.ui.main.theaters

import androidx.annotation.Keep
import soup.movie.data.model.Theater

sealed class TheatersViewState {

    @Keep
    object LoadingState : TheatersViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object ErrorState : TheatersViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    data class DoneState(val myTheaters: List<Theater>) : TheatersViewState()
}
