package soup.movie.ui.main

import soup.movie.data.model.Movie

sealed class MainUiEvent

class ShowDetailUiEvent(
    val movie: Movie
) : MainUiEvent()

object OpenDrawerMenuUiEvent : MainUiEvent()
