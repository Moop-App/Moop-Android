package soup.movie.ui.main

import soup.movie.model.Movie

sealed class MainUiEvent {

    class ShowDetailUiEvent(
        val movie: Movie
    ) : MainUiEvent()
}
