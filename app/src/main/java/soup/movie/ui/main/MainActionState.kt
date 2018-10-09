package soup.movie.ui.main

import androidx.annotation.Keep
import soup.movie.data.model.Movie

sealed class MainActionState {

    @Keep
    object NotFoundAction : MainActionState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    data class ShowDetailAction(
            val movie: Movie) : MainActionState()
}
