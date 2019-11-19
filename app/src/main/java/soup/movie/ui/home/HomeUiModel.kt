package soup.movie.ui.home

import soup.movie.data.model.Movie

class HomeHeaderUiModel(
    val isNow: Boolean
)

class HomeContentsUiModel(
    val isLoading: Boolean,
    val movies: List<Movie>,
    val hasNoItem: Boolean,
    val isError: Boolean
)
