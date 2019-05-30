package soup.movie.ui.main

import androidx.annotation.Keep
import soup.movie.data.model.Movie

sealed class MainUiEvent

@Keep
class ShowDetailUiEvent(
    val movie: Movie
) : MainUiEvent()
