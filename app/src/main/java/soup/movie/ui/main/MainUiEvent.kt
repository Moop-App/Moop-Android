package soup.movie.ui.main

import androidx.annotation.Keep
import soup.movie.data.model.Movie

sealed class MainUiEvent {

    @Keep
    object NotFoundAction : MainUiEvent() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    data class ShowDetailAction(
            val movie: Movie) : MainUiEvent()
}
