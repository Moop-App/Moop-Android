package soup.movie.ui.main

import androidx.annotation.Keep
import soup.movie.data.model.Movie

sealed class MainUiEvent {

    @Keep
    object NotFoundAction : MainUiEvent()

    @Keep
    class ShowDetailAction(
        val movie: Movie
    ) : MainUiEvent()
}
