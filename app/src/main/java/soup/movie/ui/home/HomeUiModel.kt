package soup.movie.ui.home

import soup.movie.data.model.Movie

class HomeHeaderUiModel(
    val isNow: Boolean
)

class HomeContentsUiModel(
    val movies: List<Movie>
)
